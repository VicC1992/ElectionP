package com.vote.PresidentialElection.controller;

import com.vote.PresidentialElection.entity.Candidature;
import com.vote.PresidentialElection.entity.VoteRound;
import com.vote.PresidentialElection.repository.VoteRoundRepository;
import com.vote.PresidentialElection.service.CandidatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CandidatureController {

    @Autowired
    private CandidatureService candidatureService;

    @Autowired
    private VoteRoundRepository voteRoundRepository;

    @GetMapping("/candidature/all")
    public String viewAllCandidatures(Model model, @RequestParam(required = false) Long voteRoundId) {
        List<VoteRound> voteRounds = voteRoundRepository.findAll();
        List<Candidature> candidatures = candidatureService.getAllCandidatures();

        if (voteRoundId != null) {
            model.addAttribute("selectedVoteRoundId", voteRoundId);
        }
        model.addAttribute("voteRounds", voteRounds);
        model.addAttribute("candidatures", candidatures);
        return "candidature_list";
    }

    @PostMapping("/candidature/add")
    public String addCandidature(@RequestParam long userId, Model model) {
        try {
            candidatureService.addCandidature(userId);
            model.addAttribute("message", "Candidacy submitted successfully!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/candidature/all";
    }

    @PostMapping("/candidature/delete")
    public String deleteCandidature(@RequestParam long userId, Model model) {
        try {
            candidatureService.deleteCandidature(userId);
            model.addAttribute("message", "Candidacy withdrawn successfully!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/candidature/all";
    }
}