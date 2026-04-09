package com.klu.service;

import com.klu.dto.GroupTaskDtos;
import com.klu.entity.GroupTask;
import com.klu.entity.ProjectGroup;
import com.klu.entity.TaskStatus;
import com.klu.entity.User;
import com.klu.repository.GroupTaskRepository;
import com.klu.repository.ProjectGroupRepository;
import com.klu.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupTaskService {
    private final GroupTaskRepository repo;
    private final ProjectGroupRepository groupRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public GroupTaskService(GroupTaskRepository repo, ProjectGroupRepository groupRepo, UserRepository userRepo, ModelMapper modelMapper) {
        this.repo = repo;
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    private GroupTaskDtos.RespDto mapToDto(GroupTask task) {
        GroupTaskDtos.RespDto dto = modelMapper.map(task, GroupTaskDtos.RespDto.class);
        if (task.getAssignedTo() != null) {
            dto.setAssignedToId(task.getAssignedTo().getId());
            dto.setAssignedToName(task.getAssignedTo().getName());
        }
        if (task.getStatus() != null) {
            String s = task.getStatus().name();
            if (s.equals("IN_PROGRESS")) dto.setStatus("In Progress");
            else if (s.equals("PENDING")) dto.setStatus("Pending");
            else if (s.equals("COMPLETED")) dto.setStatus("Completed");
            else dto.setStatus("Pending");
        }
        return dto;
    }

    public List<GroupTaskDtos.RespDto> getAll() {
        return repo.findAll().stream().map(this::mapToDto).toList();
    }

    public List<GroupTaskDtos.RespDto> getByGroup(Long groupId) {
        return repo.findByGroupId(groupId).stream().map(this::mapToDto).toList();
    }

    public GroupTaskDtos.RespDto create(Long groupId, GroupTaskDtos.ReqDto dto) {
        if (dto == null) throw new IllegalArgumentException("Task is required");
        
        ProjectGroup group = groupRepo.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        GroupTask task = new GroupTask();
        task.setGroup(group);
        task.setTitle(dto.getTitle());

        if (dto.getAssignedTo() != null) {
            User user = userRepo.findById(dto.getAssignedTo()).orElse(null);
            task.setAssignedTo(user);
        }

        task.setStatus(dto.getStatus() != null ? dto.getStatus() : TaskStatus.PENDING);
        task.setCreatedBy(dto.getCreatedBy());

        return mapToDto(repo.save(task));
    }

    public GroupTaskDtos.RespDto updateStatus(Long taskId, String status) {
        GroupTask task = repo.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        
        try {
            String enumStr = status.trim().toUpperCase().replace(" ", "_");
            task.setStatus(TaskStatus.valueOf(enumStr));
        } catch (IllegalArgumentException e) {
            // fallback if string is invalid
            throw new IllegalArgumentException("Invalid task status: " + status);
        }
        
        return mapToDto(repo.save(task));
    }
}

