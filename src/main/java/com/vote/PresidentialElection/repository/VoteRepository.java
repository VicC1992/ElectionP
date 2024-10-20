package com.vote.PresidentialElection.repository;

import com.vote.PresidentialElection.entity.Candidature;
import com.vote.PresidentialElection.entity.User;
import com.vote.PresidentialElection.entity.Vote;
import com.vote.PresidentialElection.entity.VoteRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByVoterAndCandidatureAndVoteRound(User voter, Candidature candidature, VoteRound voteRound);
}
