package com.martin.taskmanager.service;

import com.martin.taskmanager.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task save (Task task);
    List<Task> findAll ();
    Optional<Task> findById (Long id);

}
