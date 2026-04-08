package com.klu.dto;

import com.klu.entity.GroupStatus;
import lombok.Data;
import java.util.List;

public final class ProjectGroupDtos {
    private ProjectGroupDtos() {}

    @Data
    public static class ReqDto {
        private Long projectId;
        private List<Long> members;
        private Long leaderId;
        private GroupStatus status;
        private Integer marks;
        private Integer progress;
    }

    @Data
    public static class RespDto {
        private Long id;
        private Long projectId;
        private String projectName;
        private Long leaderId;
        private String leaderName; // Added value!
        private List<Long> memberIds; 
        private GroupStatus status;
        private Integer marks;
        private Integer progress;
        private String submissionFile;
    }
}
