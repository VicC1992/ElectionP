package com.vote.PresidentialElection.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "votes",uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "candidature_id", "vote_round_id"})})
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User voter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidature_id", nullable = false)
    private Candidature candidature;

    @ManyToOne
    @JoinColumn(name = "vote_round_id", nullable = false)
    private VoteRound voteRound;

    @Column(name = "vote_date", nullable = false)
    private LocalDateTime voteDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(LocalDateTime voteDate) {
        this.voteDate = voteDate;
    }

    public User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
        this.voter = voter;
    }

    public Candidature getCandidature() {
        return candidature;
    }

    public void setCandidature(Candidature candidature) {
        this.candidature = candidature;
    }

    public VoteRound getVoteRound() {
        return voteRound;
    }

    public void setVoteRound(VoteRound voteRound) {
        this.voteRound = voteRound;
    }
}