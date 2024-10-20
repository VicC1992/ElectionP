package com.vote.PresidentialElection.repository;

import com.vote.PresidentialElection.entity.VoteResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteResultRepository extends JpaRepository<VoteResult, Long> {
}
