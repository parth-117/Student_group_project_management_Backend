package com.klu.dto;

import com.klu.entity.TaskStatus;
import lombok.Data;

public final class GroupTaskDtos {
    private GroupTaskDtos() {}

    @Data
    public static class ReqDto {
        private String title;
        private Long assignedTo; // Raw ID from frontend
        private TaskStatus status;
        private Long createdBy;
    }

    @Data
    public static class RespDto {
        private Long id;
        private Long groupId;
        private String title;
        private Long assignedToId;
        private String assignedToName; // Added value for frontend
        private TaskStatus status;
        private Long createdBy;
    }
}
