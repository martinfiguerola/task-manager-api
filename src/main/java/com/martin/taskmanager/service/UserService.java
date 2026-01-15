package com.martin.taskmanager.service;

import com.martin.taskmanager.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save (User user);
    List<User> findAll();
    Optional<User> findById (Long id);
    Optional<User> update (Long id, User user);
    boolean deleteById (Long id);
}
