package com.klu.service;


import com.klu.entity.Project;
import com.klu.entity.ProjectGroup;
import com.klu.entity.GroupTask;
import com.klu.repository.ProjectRepository;
import com.klu.repository.ProjectGroupRepository;
import com.klu.repository.GroupTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository repo;

    @Autowired
    private ProjectGroupRepository groupRepo;

    @Autowired
    private GroupTaskRepository taskRepo;

    public Project create(Project project) {
        if (project == null) throw new IllegalArgumentException("Project is required");
        if (project.getStatus() == null || project.getStatus().isBlank()) {
            project.setStatus("Active");
        }
        if (project.getCreatedAt() == null) {
            project.setCreatedAt(LocalDate.now());
        }
        if (project.getGroupSize() == null) {
            project.setGroupSize(3);
        }
        return repo.save(project);
    }

    public List<Project> getAll() {
        return repo.findAll();
    }

    public Optional<Project> getById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        List<ProjectGroup> groups = groupRepo.findByProjectId(id);
        for (ProjectGroup group : groups) {
            List<GroupTask> tasks = taskRepo.findByGroupId(group.getId());
            taskRepo.deleteAll(tasks);
        }
        groupRepo.deleteAll(groups);
        repo.deleteById(id);

        // Optional behavior: if the deleted project was the highest ID, reuse it for the next insert
        // (i.e., avoid gaps only at the end). This does not backfill gaps in the middle.
        Long maxAfterDelete = repo.findMaxId();
        long nextId = (maxAfterDelete == null ? 1L : (maxAfterDelete + 1L));
        if (id != null && id == nextId) {
            repo.setAutoIncrement(id);
        }
    }
}
