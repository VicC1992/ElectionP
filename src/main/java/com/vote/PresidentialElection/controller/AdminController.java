package com.vote.PresidentialElection.controller;

import com.vote.PresidentialElection.entity.Role;
import com.vote.PresidentialElection.entity.User;
import com.vote.PresidentialElection.repository.RoleRepository;
import com.vote.PresidentialElection.repository.UserRepository;
import com.vote.PresidentialElection.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")

public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users_for_admin";
    }

    @GetMapping("/manage-users-roles")
    public String showAssignRoleForm(Model model) {
        List<User> users = userRepository.findAll();
        List<Role> roles = roleRepository.findAll();

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("user", new User());

        return "manage_users_roles";
    }

    @PostMapping("/assign-role")
    public String assignRoleToUser(@RequestParam Long userId, @RequestParam Long roleId) {
        userService.assignRoleToUser(userId, roleId);
        return "redirect:/admin/manage-users-roles";
    }
}
