package com.vote.PresidentialElection.service;

import com.vote.PresidentialElection.entity.Candidature;
import com.vote.PresidentialElection.entity.User;
import com.vote.PresidentialElection.entity.Vote;
import com.vote.PresidentialElection.entity.VoteRound;
import com.vote.PresidentialElection.repository.CandidatureRepository;
import com.vote.PresidentialElection.repository.UserRepository;
import com.vote.PresidentialElection.repository.VoteRepository;
import com.vote.PresidentialElection.repository.VoteRoundRepository;
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

    @Autowired
    private VoteRoundRepository voteRoundRepository;

    public void vote(Long voterId, Long candidatureId, Long voteRoundId) {
        User voter = userRepository.findById(voterId).orElseThrow(() -> new RuntimeException("Voter not found"));
        Candidature candidature = candidatureRepository.findById(candidatureId).orElseThrow(() -> new RuntimeException("Candidature not found"));
        VoteRound voteRound = voteRoundRepository.findById(voteRoundId).orElseThrow(()-> new RuntimeException("Vote round not found"));

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidature(candidature);
        vote.setVoteRound(voteRound);
        vote.setVoteDate(LocalDateTime.now());

        try {
            voteRepository.save(vote);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("You have already voted for this candidature");
        }
    }

    public void withdrawVote(Long voterId, Long candidatureId, Long voteRoundId) {
        User voter = userRepository.findById(voterId).orElseThrow(() -> new RuntimeException("Voter not found"));
        Candidature candidature = candidatureRepository.findById(candidatureId).orElseThrow(() -> new RuntimeException("Candidature not found"));
        VoteRound voteRound = voteRoundRepository.findById(voteRoundId).orElseThrow(()-> new RuntimeException("Vote round not found"));
        Optional<Vote>existingVote = voteRepository.findByVoterAndCandidatureAndVoteRound(voter, candidature, voteRound);
        if (existingVote.isPresent()) {
            voteRepository.delete(existingVote.get());
        } else {
            throw new RuntimeException("No vote found to withdraw");
        }
    }
}
