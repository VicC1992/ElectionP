package com.vote.PresidentialElection.repository;

import com.vote.PresidentialElection.entity.VoteRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRoundRepository extends JpaRepository<VoteRound, Long> {
    List<VoteRound> findByEndDateIsNull();
    List<VoteRound>findByEndDateIsNotNull();
}
