package com.vote.PresidentialElection.repository;

import com.vote.PresidentialElection.entity.VoteResult;
import com.vote.PresidentialElection.entity.VoteRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteResultRepository extends JpaRepository<VoteResult, Long> {
    List<VoteResult> findByVoteRound(VoteRound voteRound);
}
