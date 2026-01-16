package com.martin.taskmanager.bootstrap;

import com.martin.taskmanager.model.Task;
import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.TaskRepository;
import com.martin.taskmanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestRunner implements CommandLineRunner {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TestRunner(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {

            // 1️⃣ Create users
            User testUser = new User();
            testUser.setEmail("test@example.com");
            testUser.setPassword("123");

            User publicUser = new User();
            publicUser.setEmail("public@example.com");
            publicUser.setPassword("123");

            userRepository.saveAll(List.of(testUser, publicUser));

            // 2️⃣ Create tasks
            Task task = new Task();
            task.setTitle("Study Spring");
            task.setDescription("Test fetch type");
            task.setUser(testUser);

            Task task2 = new Task();
            task2.setTitle("Study Angular");
            task2.setDescription("front-end");
            task2.setUser(publicUser);


            taskRepository.saveAll(List.of(task, task2));

            // 3️⃣ Test fetch
            // taskRepository.findAll();

            System.out.println("Data loaded");
        }



    }
}
