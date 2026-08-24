package com.martin.taskmanager.repository;

import com.martin.taskmanager.model.Status;
import com.martin.taskmanager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByUserId(Long userId);
    Page<Task> findByUserEmail(String email, Pageable pageable);
    Page<Task> findByUserEmailAndStatus(String email, Status status, Pageable pageable);
    Optional<Task> findByIdAndUserEmail(Long id, String email);

}
