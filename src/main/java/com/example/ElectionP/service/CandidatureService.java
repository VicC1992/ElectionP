package com.example.ElectionP.service;

import com.example.ElectionP.entity.Candidature;
import com.example.ElectionP.entity.User;
import com.example.ElectionP.repository.CandidatureRepository;
import com.example.ElectionP.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatureService {
    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private UserRepository userRepository;

    public Candidature addCandidature(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getCandidature() != null) {
                throw new RuntimeException("User already has a candidature");
            }
            Candidature candidature = new Candidature(user);
            user.setCandidature(candidature);
            userRepository.save(user);
            return candidature;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    public void deleteCandidature(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getCandidature() != null) {
                candidatureRepository.delete(user.getCandidature());
                user.setCandidature(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("User has no candidature to delete");
            }

        } else {
            throw new RuntimeException("User not found!");
        }
    }

    public List<Candidature>getAllCandidatures() {
        return candidatureRepository.findAll();
    }
}
