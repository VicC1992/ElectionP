package com.example.ElectionP.service;

import com.example.ElectionP.entity.Candidature;
import com.example.ElectionP.entity.User;
import com.example.ElectionP.entity.Vote;
import com.example.ElectionP.repository.CandidatureRepository;
import com.example.ElectionP.repository.UserRepository;
import com.example.ElectionP.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private UserRepository userRepository;

    public void vote(Long voterId, Long candidatureId) {
        User voter = userRepository.findById(voterId).orElseThrow(() -> new RuntimeException("Voter not found"));
        Candidature candidature = candidatureRepository.findById(candidatureId).orElseThrow(() -> new RuntimeException("Candidature not found"));

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidature(candidature);
        vote.setVoteDate(LocalDateTime.now());

        try {
            voteRepository.save(vote);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("You have already voted for this candidature");
        }
    }

    public void withdrawVote(Long voterId, Long candidatureId) {
        User voter = userRepository.findById(voterId).orElseThrow(() -> new RuntimeException("Voter not found"));
        Candidature candidature = candidatureRepository.findById(candidatureId).orElseThrow(() -> new RuntimeException("Candidature not found"));
        Optional<Vote>existingVote = voteRepository.findByVoterAndCandidature(voter, candidature);
        if (existingVote.isPresent()) {
            voteRepository.delete(existingVote.get());
        } else {
            throw new RuntimeException("No vote found to withdraw");
        }
    }
}
