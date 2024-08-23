package com.example.ElectionP.controller;

import com.example.ElectionP.entity.User;
import com.example.ElectionP.repository.UserRepository;
import com.example.ElectionP.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/register/save")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/user-home")
    public String viewUserHome(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("user", user);
        return "user_home";
    }

    @GetMapping("/user/{id}")
    public String userProfile(@PathVariable(value = "id") long id, Model model) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "user_profile";
    }

    @GetMapping("/user/description-add")
    public String formAddDescription(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("user", user);
        return "user_description";
    }

    @PostMapping("/user/description-add")
    public String addDescription(User user, @AuthenticationPrincipal CustomUserDetails loggedUser,
                                 @RequestParam("description") String description, Model model) {
        loggedUser.setDescription(user.getDescription());
        return "redirect:user_home";
    }
}
