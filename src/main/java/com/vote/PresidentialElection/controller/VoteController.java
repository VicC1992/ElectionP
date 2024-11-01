package com.vote.PresidentialElection.controller;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.vote.PresidentialElection.entity.Candidature;
import com.vote.PresidentialElection.entity.User;
import com.vote.PresidentialElection.entity.VoteResult;
import com.vote.PresidentialElection.entity.VoteRound;
import com.vote.PresidentialElection.repository.CandidatureRepository;
import com.vote.PresidentialElection.repository.UserRepository;
import com.vote.PresidentialElection.repository.VoteResultRepository;
import com.vote.PresidentialElection.repository.VoteRoundRepository;
import com.vote.PresidentialElection.service.CandidatureService;
import com.vote.PresidentialElection.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private VoteRoundRepository voteRoundRepository;

    @Autowired
    private VoteResultRepository voteResultRepository;



    @PostMapping("/vote/rounds/create")
    public String createVoteRound(@RequestParam("roundName") String roundName, RedirectAttributes redirectAttributes) {
        VoteRound newRound = new VoteRound();
        newRound.setStartDate(LocalDateTime.now());
        newRound.setRoundName(roundName);
        voteRoundRepository.save(newRound);
        redirectAttributes.addFlashAttribute("roundMessage", "New voting round created successfully!");
        return "redirect:/elections/list";
    }

    @PostMapping("/vote/rounds/close/{id}")
    public String closeVoteRound(@PathVariable("id") Long roundId, RedirectAttributes redirectAttributes) {
        Optional<VoteRound> voteRoundOptional = voteRoundRepository.findById(roundId);

        if (voteRoundOptional.isPresent()) {
            VoteRound voteRound = voteRoundOptional.get();

            if (voteRound.getEndDate() == null) {
                voteRound.setEndDate(LocalDateTime.now());

                List<Candidature>candidatures = candidatureRepository.findByVoteRoundId(roundId);
                for (Candidature candidature : candidatures) {
                    VoteResult voteResult = new VoteResult();
                    voteResult.setVoteRound(voteRound);
                    voteResult.setCandidature(candidature);
                    voteResult.setFirstName(candidature.getUser().getFirstName());
                    voteResult.setLastName(candidature.getUser().getLastName());
                    voteResult.setVotesCount(candidature.getVotesReceived().size());

                    voteResultRepository.save(voteResult);
                }
                voteRoundRepository.save(voteRound);
                redirectAttributes.addFlashAttribute("roundMessage", "Voting closed successfully.");
            } else {
                redirectAttributes.addFlashAttribute("roundError", "The round has already been closed.");
            }
        } else {
            redirectAttributes.addFlashAttribute("roundError", "Round not found.");
        }
        return "redirect:/elections/list";
    }


    @PostMapping("/vote/{id}")
    public String voteCandidature(@PathVariable("id") Long candidatureId, Principal principal,@RequestParam("voteRoundId") Long voteRoundId, RedirectAttributes redirectAttributes) {
        if (voteRoundId == null || voteRoundId == 0) {
            redirectAttributes.addFlashAttribute("error", "Invalid voting round selected.");
            return "redirect:/elections/list";
        }

        VoteRound voteRound = voteRoundRepository.findById(voteRoundId)
                .orElseThrow(()-> new RuntimeException("Voting round not found."));

        LocalDateTime now = LocalDateTime.now();
        if (voteRound.getStartDate().isAfter(now) || (voteRound.getEndDate() != null && voteRound.getEndDate().isBefore(now))) {
            redirectAttributes.addFlashAttribute("error", "The selected voting round is not active.");
            return "redirect:/elections/list";
        }

        String userEmail = principal.getName();
        User currentUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        try {
            voteService.vote(currentUser.getId(), candidatureId, voteRoundId);
            redirectAttributes.addFlashAttribute("message", "You have succesfully voted!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());

        }
        return "redirect:/elections/list";
    }


    @PostMapping("/vote/{id}/withdraw")
    public String withdrawVote(@PathVariable("id") Long candidatureId, Principal principal,@RequestParam("voteRoundId") Long voteRoundId, RedirectAttributes redirectAttributes) {
        if (voteRoundId == null || voteRoundId == 0) {
            redirectAttributes.addFlashAttribute("error", "Invalid voting round selected.");
            return "redirect:/elections/list";
        }
        VoteRound voteRound = voteRoundRepository.findById(voteRoundId)
                .orElseThrow(()-> new RuntimeException("Voting round not found."));
        LocalDateTime now = LocalDateTime.now();
        if (voteRound.getStartDate().isAfter(now) || (voteRound.getEndDate() != null && voteRound.getEndDate().isBefore(now))) {
            redirectAttributes.addFlashAttribute("error", "The selected voting round is not active.");
            return "redirect:/elections/list";
        }

        String userEmail = principal.getName();
        User currentUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        try {
            voteService.withdrawVote(currentUser.getId(), candidatureId, voteRoundId);
            redirectAttributes.addFlashAttribute("message", "Your vote has been successfully withdraw");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/elections/list";
    }
}