package com.vote.PresidentialElection.controller;

import com.vote.PresidentialElection.entity.User;
import com.vote.PresidentialElection.entity.VoteRound;
import com.vote.PresidentialElection.repository.UserRepository;
import com.vote.PresidentialElection.repository.VoteRoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRoundRepository voteRoundRepository;

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

    @GetMapping("/user/{id}")
    public String userProfile(@PathVariable(value = "id") long id, Model model, Principal principal) {
        String loggedInUsername = principal.getName();
        User loggedInUser = userRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID" + id + "not found"));

        List<VoteRound> voteRounds = voteRoundRepository.findAll();
        model.addAttribute("voteRounds", voteRounds);
        model.addAttribute("user", user);
        model.addAttribute("loggedInUser", loggedInUser);
        return "user_profile";
    }

    @GetMapping("/user/description/add")
    public String formAddDescription(@RequestParam("userId") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID" + id + "not found"));
        model.addAttribute("user", user);
        return "edit_profile";
    }

    @PostMapping("/user/update")
    public String saveUser(@ModelAttribute("user") User user) {
        userRepository.save(user);
        return "redirect:/user/" + user.getId();
    }

}