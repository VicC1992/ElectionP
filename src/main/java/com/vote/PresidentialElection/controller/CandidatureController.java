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

    @GetMapping("/candidature/list")
    public String viewCandidaturesByRound(Model model, @RequestParam Long voteRoundId) {
        List<Candidature>candidatures = candidatureService.getCandidaturesByVoteRoundId(voteRoundId);
        model.addAttribute("selectedVoteRoundId", voteRoundId);
        model.addAttribute("candidatures", candidatures);
        return "candidate_list";
    }

    @PostMapping("/candidature/add")
    public String addCandidature(@RequestParam long userId, @RequestParam long voteRoundId, Model model) {
        try {
            candidatureService.addCandidature(userId, voteRoundId);
            model.addAttribute("message", "Candidacy submitted successfully!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/elections/list";
    }

    @PostMapping("/candidature/delete")
    public String deleteCandidature(@RequestParam long userId,@RequestParam long voteRoundId, Model model) {
        try {
            candidatureService.deleteCandidature(userId , voteRoundId);
            model.addAttribute("message", "Candidacy withdrawn successfully!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/elections/list";
    }
}