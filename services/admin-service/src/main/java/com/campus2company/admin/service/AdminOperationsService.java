package com.campus2company.admin.service;

import com.campus2company.admin.client.AuthProvisioningClient;
import com.campus2company.admin.dto.AuthUserResponse;
import com.campus2company.admin.dto.CreateAdminAccountRequest;
import com.campus2company.admin.dto.CreateLecturerRequest;
import com.campus2company.admin.dto.LecturerResponse;
import com.campus2company.admin.dto.ProjectLecturerAssignmentResponse;
import com.campus2company.admin.dto.ProvisionAccountRequest;
import com.campus2company.admin.exception.ApiException;
import com.campus2company.admin.exception.LecturerNotFoundException;
import com.campus2company.admin.model.Lecturer;
import com.campus2company.admin.model.ProjectLecturerAssignment;
import com.campus2company.admin.repository.LecturerRepository;
import com.campus2company.admin.repository.ProjectLecturerAssignmentRepository;
import com.campus2company.common.model.Role;
import com.campus2company.common.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminOperationsService {

    private final AuthProvisioningClient authProvisioningClient;
    private final LecturerRepository lecturerRepository;
    private final ProjectLecturerAssignmentRepository projectLecturerAssignmentRepository;

    @Transactional
    public AuthUserResponse createAdminAccount(
            CreateAdminAccountRequest request,
            UserPrincipal admin,
            String authorizationHeader) {
        Role role = request.getRole();
        if (role != Role.UNIVERSITY_ADMIN && role != Role.PLATFORM_ADMIN) {
            throw new ApiException(
                    "Target role must be UNIVERSITY_ADMIN or PLATFORM_ADMIN",
                    HttpStatus.BAD_REQUEST);
        }

        validateProvisioning(admin.getRole(), role);

        ProvisionAccountRequest provision = new ProvisionAccountRequest(
                request.getEmail(),
                request.getPassword(),
                role);

        return authProvisioningClient.provisionAccount(provision, authorizationHeader);
    }

    @Transactional
    public LecturerResponse createLecturer(
            CreateLecturerRequest request,
            UserPrincipal admin,
            String authorizationHeader) {
        validateProvisioning(admin.getRole(), Role.LECTURER);

        ProvisionAccountRequest provision = new ProvisionAccountRequest(
                request.getEmail(),
                request.getPassword(),
                Role.LECTURER);
        AuthUserResponse auth = authProvisioningClient.provisionAccount(provision, authorizationHeader);

        Lecturer lecturer = Lecturer.builder()
                .authUserId(auth.getId())
                .email(auth.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .createdAt(Instant.now())
                .build();

        lecturerRepository.save(lecturer);
        return LecturerResponse.from(lecturer);
    }

    @Transactional
    public ProjectLecturerAssignmentResponse assignLecturerToProject(
            UUID projectId,
            UUID lecturerAuthUserId,
            UserPrincipal admin) {

        lecturerRepository.findById(lecturerAuthUserId)
                .orElseThrow(() -> new LecturerNotFoundException(lecturerAuthUserId));

        if (projectLecturerAssignmentRepository.existsByProjectIdAndLecturerAuthUserId(projectId, lecturerAuthUserId)) {
            throw new ApiException("Lecturer is already assigned to this project", HttpStatus.CONFLICT);
        }

        ProjectLecturerAssignment entity = ProjectLecturerAssignment.builder()
                .projectId(projectId)
                .lecturerAuthUserId(lecturerAuthUserId)
                .assignedAt(Instant.now())
                .assignedByAdminId(admin.getId())
                .build();

        ProjectLecturerAssignment saved = projectLecturerAssignmentRepository.save(entity);
        return ProjectLecturerAssignmentResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<ProjectLecturerAssignmentResponse> listLecturersForProject(UUID projectId) {
        return projectLecturerAssignmentRepository.findByProjectId(projectId).stream()
                .map(ProjectLecturerAssignmentResponse::from)
                .toList();
    }

    private void validateProvisioning(Role callerRole, Role targetRole) {
        // Mirror auth-service rules:
        // - UNIVERSITY_ADMIN may create UNIVERSITY_ADMIN or LECTURER
        // - PLATFORM_ADMIN may create anything except STUDENT/EMPLOYER via this flow
        if (callerRole == Role.UNIVERSITY_ADMIN) {
            if (targetRole != Role.UNIVERSITY_ADMIN && targetRole != Role.LECTURER) {
                throw new ApiException(
                        "University admins may only create university admins or lecturers",
                        HttpStatus.FORBIDDEN);
            }
            return;
        }

        if (callerRole == Role.PLATFORM_ADMIN) {
            if (targetRole == Role.STUDENT || targetRole == Role.EMPLOYER) {
                throw new ApiException(
                        "Students and employers must register through the public registration flow",
                        HttpStatus.FORBIDDEN);
            }
            return;
        }

        throw new ApiException("Provisioning is not allowed for this account", HttpStatus.FORBIDDEN);
    }
}
