package com.klu.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GroupStatus {
    WORKING, 
    SUBMITTED, 
    GRADED;

    @JsonCreator
    public static GroupStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return GroupStatus.valueOf(value.trim().toUpperCase());
    }
}
