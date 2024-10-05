package com.example.ElectionP.controller;

import com.example.ElectionP.entity.User;
import com.example.ElectionP.repository.CandidatureRepository;
import com.example.ElectionP.repository.UserRepository;
import com.example.ElectionP.service.CandidatureService;
import com.example.ElectionP.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidatureService candidatureService;

    @PostMapping("/vote/{id}")
    public String voteCandidature(@PathVariable("id") Long candidatureId, Principal principal, RedirectAttributes redirectAttributes) {

        String userEmail = principal.getName();
        User currentUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        try {
            voteService.vote(currentUser.getId(), candidatureId);
            redirectAttributes.addFlashAttribute("message", "You have succesfully voted!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());

        }
        return "redirect:/candidature/all";
    }
}
