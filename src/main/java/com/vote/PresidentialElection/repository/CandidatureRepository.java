package com.vote.PresidentialElection.repository;

import com.vote.PresidentialElection.entity.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
}