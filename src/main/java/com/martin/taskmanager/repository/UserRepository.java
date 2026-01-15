package com.martin.taskmanager.repository;

import com.martin.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
