package com.klu.repository;

import com.klu.entity.GroupTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupTaskRepository extends JpaRepository<GroupTask, Long> {
    List<GroupTask> findByGroupId(Long groupId);
}

