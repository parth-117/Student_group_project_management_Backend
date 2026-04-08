package com.klu.controller;



import com.klu.dto.ProjectGroupDtos;
import com.klu.entity.Project;
import com.klu.service.ProjectGroupService;
import com.klu.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService service;

    @Autowired
    private ProjectGroupService groupService;

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project project) {
        return ResponseEntity.ok(service.create(project));
    }

    @GetMapping
    public List<Project> getAll() {
        return service.getAll();
    }

    @GetMapping("/{projectId}/groups")
    public ResponseEntity<List<ProjectGroupDtos.RespDto>> groups(@PathVariable Long projectId) {
        return ResponseEntity.ok(groupService.getByProject(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getOne(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
