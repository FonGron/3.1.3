package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    Object getUserById(Long id);
    void deleteUser(Long id);
    void addUser(User user);
    void editUser(User user);
}
