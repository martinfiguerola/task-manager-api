package com.martin.taskmanager.repository;

import com.martin.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByUserId(Long userId);
}
