package com.klu.controller;

import com.klu.dto.GroupTaskDtos;
import com.klu.service.GroupTaskService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final GroupTaskService service;

    @Autowired
    public TaskController(GroupTaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<GroupTaskDtos.RespDto> getAll() {
        return service.getAll();
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<GroupTaskDtos.RespDto> updateStatus(@PathVariable Long taskId, @RequestBody StatusRequest body) {
        if (body == null || body.getStatus() == null || body.getStatus().isBlank()) {
            throw new IllegalArgumentException("status is required");
        }
        return ResponseEntity.ok(service.updateStatus(taskId, body.getStatus()));
    }

    @PostMapping("/group/{groupId}")
    public ResponseEntity<GroupTaskDtos.RespDto> create(@PathVariable Long groupId, @RequestBody GroupTaskDtos.ReqDto task) {
        return ResponseEntity.ok(service.create(groupId, task));
    }

    @Data
    public static class StatusRequest {
        private String status;
    }
}

