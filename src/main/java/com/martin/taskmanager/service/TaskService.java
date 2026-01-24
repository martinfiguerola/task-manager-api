package com.martin.taskmanager.service;

import com.martin.taskmanager.model.Task;

import java.util.List;

public interface TaskService {

    Task save (Task task);
    List<Task> findAll ();
}
