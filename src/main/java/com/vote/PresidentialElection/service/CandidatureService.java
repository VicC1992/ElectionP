package com.vote.PresidentialElection.service;

import com.vote.PresidentialElection.entity.Candidature;
import com.vote.PresidentialElection.entity.User;
import com.vote.PresidentialElection.entity.VoteRound;
import com.vote.PresidentialElection.repository.CandidatureRepository;
import com.vote.PresidentialElection.repository.UserRepository;
import com.vote.PresidentialElection.repository.VoteRoundRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CandidatureService {
    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private VoteRoundRepository voteRoundRepository;

    @Autowired
    private UserRepository userRepository;

    public Candidature addCandidature(Long userId, Long voteRoundId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<VoteRound> voteRoundOptional = voteRoundRepository.findById(voteRoundId);

        if (userOptional.isPresent() && voteRoundOptional.isPresent()) {
            User user = userOptional.get();
            VoteRound voteRound = voteRoundOptional.get();

            if (voteRound.getEndDate() != null) {
                throw new RuntimeException("Cannot add candidature. The voting round is closed.");
            }

            if (user.getCandidatures().stream().anyMatch(c -> c.getVoteRound().getId().equals(voteRoundId))) {
                throw new RuntimeException("User already has a candidature for this voting round");
            }

            Candidature candidature = new Candidature();
            candidature.setUser(user);
            candidature.setVoteRound(voteRoundOptional.get());
            candidature.setSubmissionDate(LocalDateTime.now());

            user.getCandidatures().add(candidature);
            candidatureRepository.save(candidature);
            userRepository.save(user);

            return candidature;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    public void deleteCandidature(Long userId, Long voteRoundId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<VoteRound> voteRoundOptional = voteRoundRepository.findById(voteRoundId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            VoteRound voteRound = voteRoundOptional.get();
            if (voteRound.getEndDate() != null) {
                throw new RuntimeException("Cannot add candidature. The voting round is closed.");
            }
            if (!user.getCandidatures().isEmpty()) {
                Candidature candidatureToDelete = user.getCandidatures().get(0);
                user.getCandidatures().remove(candidatureToDelete);
                candidatureRepository.delete(candidatureToDelete);
                userRepository.save(user);
            } else {
                throw new RuntimeException("User has no candidature to delete");
            }

        } else {
            throw new RuntimeException("User not found!");
        }
    }

    public List<Candidature> getAllCandidatures() {
        return candidatureRepository.findAll();
    }

    public List<Candidature>getCandidaturesByVoteRoundId(Long voteRoundId) {
        if (voteRoundId == null) {
            return candidatureRepository.findAll();
        } else {
            return candidatureRepository.findByVoteRoundId(voteRoundId);
        }
    }
}