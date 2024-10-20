package com.vote.PresidentialElection;


import com.vote.PresidentialElection.entity.VoteRound;
import com.vote.PresidentialElection.repository.VoteRoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VoteRoundInitializer implements CommandLineRunner {
    @Autowired
    private VoteRoundRepository voteRoundRepository;

    @Override
    public void run(String... args) throws Exception{
        if (voteRoundRepository.count() == 0) {
            VoteRound initialRound = new VoteRound();
            initialRound.setRoundName("Round number 1");
            initialRound.setStartDate(LocalDateTime.now());
            voteRoundRepository.save(initialRound);
            System.out.println("Default voting round created.");
        } else {
            System.out.println("Already existing voting round.");
        }
    }
}
