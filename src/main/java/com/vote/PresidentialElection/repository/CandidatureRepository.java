package com.vote.PresidentialElection.repository;

import com.vote.PresidentialElection.entity.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature>findByVoteRoundId(Long voteRoundId);
}