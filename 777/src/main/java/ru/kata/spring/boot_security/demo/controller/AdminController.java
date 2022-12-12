package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin")
    public String allUsers(ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        return "allUsers";
    }

    @GetMapping(value = "/admin/new")
    public String addUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "newUser";
    }
    @PostMapping(value = "/admin/new")
    public String postAddUser(@ModelAttribute("user") User user,
                              @RequestParam(required=false) String roleUser,
                              @RequestParam(required=false) String roleAdmin) {
        Set<Role> roles = new HashSet<>();
        if (roleAdmin != null && roleAdmin.equals("ADMIN")) {
            roles.add(roleService.getRoleByName("ADMIN"));
        }
        if (roleUser != null && roleUser.equals("USER")) {
            roles.add(roleService.getRoleByName("USER"));
        }
        user.setRoles(roles);
        userService.addUser(user);

        return "redirect:/admin";
    }
    @GetMapping(value = "/admin/edit/{id}")
    public String editUser(ModelMap model, @PathVariable("id") Long id) {
        model.addAttribute("user",userService.getUserById(id));
        return "editUser";
    }
    @PostMapping(value = "admin/edit/{id}")
    public String postEditUser(@ModelAttribute("user") User user,
                               @RequestParam(required=false) String roleAdmin,
                               @RequestParam(required=false) String roleUser) {
        Set<Role> roles = new HashSet<>();
        if (roleAdmin != null && roleAdmin.equals("ADMIN")) {
            roles.add(roleService.getRoleByName("ADMIN"));
        }
        if (roleUser != null && roleUser.equals("USER")) {
            roles.add(roleService.getRoleByName("USER"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        userService.editUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        System.out.println(id);
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
