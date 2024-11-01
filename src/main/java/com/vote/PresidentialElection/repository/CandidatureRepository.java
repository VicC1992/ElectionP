package com.vote.PresidentialElection.repository;

import com.vote.PresidentialElection.entity.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    @Query("SELECT c FROM Candidature c WHERE c.voteRound.id = :voteRoundId ORDER BY SIZE(c.votesReceived) DESC")
    List<Candidature>findByVoteRoundId(@Param("voteRoundId") Long voteRoundId);
}