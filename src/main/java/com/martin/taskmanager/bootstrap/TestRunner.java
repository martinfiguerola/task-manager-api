package com.martin.taskmanager.bootstrap;

import com.martin.taskmanager.model.Task;
import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.TaskRepository;
import com.martin.taskmanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

        // 1️⃣ Create user
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("123");

        userRepository.save(user);

        // 2️⃣ Create t
        Task task = new Task();
        task.setTitle("Study Spring");
        task.setDescription("Test fetch type");
        task.setUser(user);

        taskRepository.save(task);

        // 3️⃣ Test fetch
        taskRepository.findAll();

        System.out.println("Data loaded and query executed");

    }
}
