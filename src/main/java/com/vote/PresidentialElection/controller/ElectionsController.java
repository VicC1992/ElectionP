package com.vote.PresidentialElection.controller;

import com.vote.PresidentialElection.entity.Candidature;
import com.vote.PresidentialElection.entity.VoteResult;
import com.vote.PresidentialElection.entity.VoteRound;
import com.vote.PresidentialElection.repository.CandidatureRepository;
import com.vote.PresidentialElection.repository.VoteResultRepository;
import com.vote.PresidentialElection.repository.VoteRoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ElectionsController {

    @Autowired
    private VoteRoundRepository voteRoundRepository;

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private VoteResultRepository voteResultRepository;

    @GetMapping("/elections/list")
    public String viewActiveRounds(Model model) {
        List<VoteRound> voteRounds = voteRoundRepository.findByEndDateIsNull();
        model.addAttribute("voteRounds", voteRounds);
        return "active_elections";
    }

    @GetMapping("/elections/closed")
    public String viewClosedRoundsWithResults(Model model) {
        List<VoteRound> closedRounds = voteRoundRepository.findByEndDateIsNotNull();
        Map<VoteRound, List<VoteResult>> roundResults = new HashMap<>();

        for (VoteRound round : closedRounds) {
            List<VoteResult> results = voteResultRepository.findByVoteRound(round);
            roundResults.put(round, results);
        }

        model.addAttribute("roundResults", roundResults);
        return "closed_elections";
    }

    @GetMapping("/elections/closed/{id}")
    public String viewResultsForRound(@PathVariable("id") Long roundId, Model model) {
        Optional<VoteRound> voteRoundOptional = voteRoundRepository.findById(roundId);

        if (voteRoundOptional.isPresent()) {
            VoteRound voteRound = voteRoundOptional.get();
            List<VoteResult> results = voteResultRepository.findByVoteRound(voteRound);

            model.addAttribute("voteRound", voteRound);
            model.addAttribute("results", results);
            return "round_results";
        } else {
            model.addAttribute("error", "Round not found.");
            return "redirect:/elections/list";
        }
    }

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

                List<Candidature> candidatures = candidatureRepository.findByVoteRoundId(roundId);
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
}
