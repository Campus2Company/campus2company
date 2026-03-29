package com.campus2company.project.repository;


import com.campus2company.common.enums.ProjectCategory;
import com.campus2company.common.enums.ProjectStatus;
import com.campus2company.common.enums.StudyLevel;
import com.campus2company.project.model.Project;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ProjectSpecification {

    public static Specification<Project> hasStatus(ProjectStatus status) {
        return (Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Project> hasCategory(ProjectCategory category) {
        return (Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (category == null) {
                return cb.conjunction();
            }
            return cb.isMember(category, root.get("categories"));
        };
    }

    public static Specification<Project> hasStudyLevel(StudyLevel studyLevel) {
        return (Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (studyLevel == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("studyLevel"), studyLevel);
        };
    }

    public static Specification<Project> hasEmployerId(UUID employerId) {
        return (Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (employerId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("employerId"), employerId);
        };
    }
}
