package com.vote.PresidentialElection.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidatures")
public class Candidature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "submission_date")
    private LocalDateTime submissionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "candidature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votesReceived = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "vote_round_id", nullable = false)
    private VoteRound voteRound;

    public VoteRound getVoteRound() {
        return voteRound;
    }

    public void setVoteRound(VoteRound voteRound) {
        this.voteRound = voteRound;
    }

    public List<Vote> getVotesReceived() {
        return votesReceived;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Candidature() {
    }
}