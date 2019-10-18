package com.app.services;

import com.app.entities.Scoreboard;
import com.app.repo.ScoreboardRepo;
import com.auth.entities.User;
import com.auth.repo.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class ScoreboardSaver {

    private final ScoreboardRepo scoreboardRepo;
    private final UserRepo userRepo;

    public ScoreboardSaver(ScoreboardRepo scoreboardRepo, UserRepo userRepo) {
        this.scoreboardRepo = scoreboardRepo;
        this.userRepo = userRepo;
    }

    public void storeScoreboard(String winner, String loser){
        storeData(winner, true);
        storeData(loser, false);
    }

    private void storeData(String username, boolean isWinner){
        User user = userRepo.findByUsername(username);
        if (user == null){
            throw new IllegalArgumentException();
        } else if (user.getScoreboard() == null){
            Scoreboard newRecord = new Scoreboard();
            if (isWinner){
                newRecord.setWins(1);
                newRecord.setLoses(0);
            } else {
                newRecord.setWins(0);
                newRecord.setLoses(1);
            }
            newRecord.setUser(user);
            user.setScoreboard(newRecord);
            scoreboardRepo.save(newRecord);
        } else {
            Scoreboard recordInDb = user.getScoreboard();
            if (isWinner){
                recordInDb.setWins(recordInDb.getWins() + 1);
            } else {
                recordInDb.setLoses(recordInDb.getLoses() + 1);
            }
            userRepo.save(user);
        }
    }

}
