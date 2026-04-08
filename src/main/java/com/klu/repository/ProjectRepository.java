package com.klu.repository;



import com.klu.entity.Project;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select max(p.id) from Project p")
    Long findMaxId();

    @Modifying
    @Query(value = "ALTER TABLE project AUTO_INCREMENT = :nextId", nativeQuery = true)
    void setAutoIncrement(@Param("nextId") long nextId);
}
