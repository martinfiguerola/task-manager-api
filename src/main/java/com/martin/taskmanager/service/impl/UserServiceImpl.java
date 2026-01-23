package com.martin.taskmanager.service.impl;

import com.martin.taskmanager.model.User;
import com.martin.taskmanager.repository.UserRepository;
import com.martin.taskmanager.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public Optional<User> update(Long id, User user) {

        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser
                .map(existsUser -> {

                    existsUser.setEmail(user.getEmail());
                    existsUser.setPassword(user.getPassword());

                    return userRepository.save(existsUser);
                });
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                }).orElse(false);
    }
}
