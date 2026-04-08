package com.klu.repository;

import com.klu.entity.ProjectGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectGroupRepository extends JpaRepository<ProjectGroup, Long> {
    List<ProjectGroup> findByProjectId(Long projectId);
}

