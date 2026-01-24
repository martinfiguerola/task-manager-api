package com.martin.taskmanager.controller;

import com.martin.taskmanager.model.Task;

import com.martin.taskmanager.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask (@RequestBody Task task) {
        Task savedTask = taskService.save(task);
        return ResponseEntity.ok(savedTask);
    }
}
