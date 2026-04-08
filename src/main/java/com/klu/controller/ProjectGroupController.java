package com.klu.controller;

import com.klu.dto.ProjectGroupDtos;
import com.klu.service.ProjectGroupService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class ProjectGroupController {

    private final ProjectGroupService service;

    @Autowired
    public ProjectGroupController(ProjectGroupService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProjectGroupDtos.RespDto> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<ProjectGroupDtos.RespDto> create(@RequestBody ProjectGroupDtos.ReqDto group) {
        return ResponseEntity.ok(service.create(group));
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<ProjectGroupDtos.RespDto> patch(@PathVariable Long groupId, @RequestBody MarksRequest body) {
        if (body == null || body.getMarks() == null) throw new IllegalArgumentException("marks is required");
        return ResponseEntity.ok(service.assignMarks(groupId, body.getMarks()));
    }

    @PostMapping(path = "/{groupId}/submission", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectGroupDtos.RespDto> submit(@PathVariable Long groupId, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.submit(groupId, file));
    }

    @GetMapping("/{groupId}/submission")
    public ResponseEntity<Resource> download(@PathVariable Long groupId) {
        Resource resource = service.downloadSubmission(groupId);
        String fileName = resource.getFilename() == null ? "submission" : resource.getFilename();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/{groupId}/submission")
    public ResponseEntity<ProjectGroupDtos.RespDto> deleteSubmission(@PathVariable Long groupId) throws IOException {
        return ResponseEntity.ok(service.deleteSubmission(groupId));
    }

    @Data
    public static class MarksRequest {
        private Integer marks;
    }
}

