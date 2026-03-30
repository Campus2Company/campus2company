package com.campus2company.application.service;

import com.campus2company.application.dto.request.CreateApplicationRequest;
import com.campus2company.application.dto.request.UpdateApplicationStatusRequest;
import com.campus2company.application.dto.response.ApplicationResponse;
import com.campus2company.application.exception.DuplicateApplicationException;
import com.campus2company.application.exception.InvalidStatusTransitionException;
import com.campus2company.application.exception.ResourceNotFoundException;
import com.campus2company.application.model.Application;
import com.campus2company.application.repository.ApplicationRepository;
import com.campus2company.common.enums.ApplicationStatus;
import com.campus2company.common.event.ApplicationStatusChangedEvent;
import com.campus2company.common.event.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public ApplicationResponse createApplication(UUID studentId, CreateApplicationRequest request) {
        if (repository.existsByStudentIdAndProjectId(studentId, request.getProjectId())) {
            throw new DuplicateApplicationException();
        }

        Application application = Application.builder()
                .studentId(studentId)
                .projectId(request.getProjectId())
                .employerId(request.getEmployerId())
                .projectTitle(request.getProjectTitle())
                .coverLetter(request.getCoverLetter())
                .status(ApplicationStatus.PENDING)
                .build();

        Application saved = repository.save(application);
        log.info("Student {} applied to project {} (application {})", studentId, request.getProjectId(), saved.getId());

        publishStatusEvent(saved, null, ApplicationStatus.PENDING);

        return ApplicationResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getStudentApplications(UUID studentId, Pageable pageable) {
        return repository.findByStudentId(studentId, pageable)
                .map(ApplicationResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getApplicationsForProject(UUID employerId, UUID projectId, Pageable pageable) {
        return repository.findByProjectId(projectId, pageable)
                .map(ApplicationResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getEmployerApplications(UUID employerId, Pageable pageable) {
        return repository.findByEmployerId(employerId, pageable)
                .map(ApplicationResponse::from);
    }

    @Transactional
    public ApplicationResponse updateApplicationStatus(UUID employerId, UUID applicationId, UpdateApplicationStatusRequest request) {
        Application application = repository.findByIdAndEmployerId(applicationId, employerId)
                .orElseThrow(() -> ResourceNotFoundException.application(applicationId));

        ApplicationStatus newStatus = request.getStatus();

        if (newStatus != ApplicationStatus.ACCEPTED && newStatus != ApplicationStatus.REJECTED) {
            throw new InvalidStatusTransitionException("Employer can only ACCEPT or REJECT an application");
        }

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new InvalidStatusTransitionException(
                    "Can only accept/reject PENDING applications. Current status: " + application.getStatus());
        }

        ApplicationStatus previousStatus = application.getStatus();
        application.setStatus(newStatus);
        Application saved = repository.save(application);

        log.info("Application {} status changed: {} -> {} by employer {}",
                applicationId, previousStatus, newStatus, employerId);

        publishStatusEvent(saved, previousStatus, newStatus);

        return ApplicationResponse.from(saved);
    }

    @Transactional
    public ApplicationResponse withdrawApplication(UUID studentId, UUID applicationId) {
        Application application = repository.findByIdAndStudentId(applicationId, studentId)
                .orElseThrow(() -> ResourceNotFoundException.application(applicationId));

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new InvalidStatusTransitionException(
                    "Can only withdraw PENDING applications. Current status: " + application.getStatus());
        }

        ApplicationStatus previousStatus = application.getStatus();
        application.setStatus(ApplicationStatus.WITHDRAWN);
        Application saved = repository.save(application);

        log.info("Application {} withdrawn by student {}", applicationId, studentId);

        publishStatusEvent(saved, previousStatus, ApplicationStatus.WITHDRAWN);

        return ApplicationResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getApplicationById(UUID applicationId) {
        Application application = repository.findById(applicationId)
                .orElseThrow(() -> ResourceNotFoundException.application(applicationId));
        return ApplicationResponse.from(application);
    }

    private void publishStatusEvent(Application app, ApplicationStatus previousStatus, ApplicationStatus newStatus) {
        try {
            ApplicationStatusChangedEvent event = ApplicationStatusChangedEvent.builder()
                    .eventId(UUID.randomUUID())
                    .eventType("APPLICATION_STATUS_CHANGED")
                    .timestamp(LocalDateTime.now())
                    .source("application-service")
                    .applicationId(app.getId())
                    .studentId(app.getStudentId())
                    .employerId(app.getEmployerId())
                    .projectId(app.getProjectId())
                    .projectTitle(app.getProjectTitle())
                    .previousStatus(previousStatus != null ? previousStatus.name() : null)
                    .newStatus(newStatus.name())
                    .build();

            kafkaTemplate.send(KafkaTopics.APPLICATION_STATUS_CHANGED, app.getId().toString(), event);
            log.debug("Published application status event for application {}", app.getId());
        } catch (Exception e) {
            log.error("Failed to publish application status event for {}: {}", app.getId(), e.getMessage());
        }
    }
}
