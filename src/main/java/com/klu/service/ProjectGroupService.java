package com.klu.service;

import com.klu.dto.ProjectGroupDtos;
import com.klu.entity.GroupStatus;
import com.klu.entity.Project;
import com.klu.entity.ProjectGroup;
import com.klu.entity.User;
import com.klu.repository.ProjectGroupRepository;
import com.klu.repository.ProjectRepository;
import com.klu.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class ProjectGroupService {
    private final ProjectGroupRepository repo;
    private final UserRepository userRepo;
    private final ProjectRepository projectRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public ProjectGroupService(ProjectGroupRepository repo, UserRepository userRepo, ProjectRepository projectRepo, ModelMapper modelMapper) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
        this.modelMapper = modelMapper;
    }

    private ProjectGroupDtos.RespDto mapToDto(ProjectGroup group) {
        ProjectGroupDtos.RespDto dto = modelMapper.map(group, ProjectGroupDtos.RespDto.class);
        dto.setProjectName(group.getProjectName());
        if (group.getMembers() != null) {
            dto.setMemberIds(group.getMembers().stream().map(User::getId).toList());
        }
        return dto;
    }

    public List<ProjectGroupDtos.RespDto> getAll() {
        return repo.findAll().stream().map(this::mapToDto).toList();
    }

    public List<ProjectGroupDtos.RespDto> getByProject(Long projectId) {
        return repo.findByProjectId(projectId).stream().map(this::mapToDto).toList();
    }

    public ProjectGroupDtos.RespDto create(ProjectGroupDtos.ReqDto dto) {
        if (dto == null) throw new IllegalArgumentException("Group is required");
        if (dto.getProjectId() == null) throw new IllegalArgumentException("projectId is required");
        
        Project project = projectRepo.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        ProjectGroup group = new ProjectGroup();
        group.setProject(project);
        group.setProjectName(project.getTitle());

        if (dto.getMembers() != null && !dto.getMembers().isEmpty()) {
            List<User> users = userRepo.findAllById(dto.getMembers());
            group.setMembers(users);
        }

        Long leaderId = dto.getLeaderId();
        if (leaderId == null && dto.getMembers() != null && !dto.getMembers().isEmpty()) {
            leaderId = dto.getMembers().get(0);
        }
        if (leaderId != null) {
            User leader = userRepo.findById(leaderId).orElse(null);
            group.setLeader(leader);
        }

        group.setStatus(dto.getStatus() != null ? dto.getStatus() : GroupStatus.WORKING);
        group.setProgress(dto.getProgress() != null ? dto.getProgress() : 0);
        
        return mapToDto(repo.save(group));
    }

    public ProjectGroupDtos.RespDto assignMarks(Long groupId, int marks) {
        if (marks < 0 || marks > 100) throw new IllegalArgumentException("Marks must be between 0 and 100");
        ProjectGroup group = repo.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        group.setMarks(marks);
        return mapToDto(repo.save(group));
    }

    public ProjectGroupDtos.RespDto submit(Long groupId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("File is required");
        ProjectGroup group = repo.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));

        Path uploadsRoot = Paths.get("uploads", "groups", String.valueOf(groupId));
        Files.createDirectories(uploadsRoot);

        String original = file.getOriginalFilename();
        String fileName = (original == null || original.isBlank()) ? "submission" : original;
        Path destination = uploadsRoot.resolve(fileName).normalize();
        Files.write(destination, file.getBytes());

        group.setSubmissionFile(fileName);
        group.setSubmissionPath(destination.toString());
        group.setStatus(GroupStatus.SUBMITTED);
        group.setProgress(100);
        return mapToDto(repo.save(group));
    }

    public Resource downloadSubmission(Long groupId) {
        ProjectGroup group = repo.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        if (group.getSubmissionPath() == null || group.getSubmissionPath().isBlank()) {
            throw new IllegalArgumentException("No submission found");
        }
        return new FileSystemResource(group.getSubmissionPath());
    }

    public ProjectGroupDtos.RespDto deleteSubmission(Long groupId) throws IOException {
        ProjectGroup group = repo.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (group.getSubmissionPath() != null && !group.getSubmissionPath().isBlank()) {
            try {
                Files.deleteIfExists(Path.of(group.getSubmissionPath()));
            } catch (IOException ignored) {
                // ignore file deletion errors, still clear DB fields
            }
        }

        group.setSubmissionFile(null);
        group.setSubmissionPath(null);
        group.setStatus(GroupStatus.WORKING);
        group.setProgress(0);
        group.setMarks(null);
        return mapToDto(repo.save(group));
    }
}
