package com.example.ElectionP.repository;

import com.example.ElectionP.entity.Candidature;
import com.example.ElectionP.entity.User;
import com.example.ElectionP.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByVoterAndCandidature(User voter, Candidature candidature);
}
