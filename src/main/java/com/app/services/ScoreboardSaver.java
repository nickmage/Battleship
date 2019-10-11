package com.app.services;

import com.app.entities.Scoreboard;
import com.app.repo.ScoreboardRepo;
import org.springframework.stereotype.Service;

@Service
public class ScoreboardSaver {

    private final ScoreboardRepo scoreboardRepo;

    public ScoreboardSaver(ScoreboardRepo scoreboardRepo) {
        this.scoreboardRepo = scoreboardRepo;
    }

    public void storeScoreboard(String winner, String loser){
        storeWinner(winner);
        storeLoser(loser);
    }

    private void storeWinner(String username){
        Scoreboard recordInDb = scoreboardRepo.findByUsername(username);
        if (recordInDb == null){
            Scoreboard newRecord = new Scoreboard();
            newRecord.setUsername(username);
            newRecord.setWins(1);
            newRecord.setLoses(0);
            scoreboardRepo.save(newRecord);
        } else {
            recordInDb.setWins(recordInDb.getWins() + 1);
            scoreboardRepo.save(recordInDb);
        }
    }

    private void storeLoser(String username){
        Scoreboard recordInDb = scoreboardRepo.findByUsername(username);
        if (recordInDb == null){
            Scoreboard newRecord = new Scoreboard();
            newRecord.setUsername(username);
            newRecord.setWins(0);
            newRecord.setLoses(1);
            scoreboardRepo.save(newRecord);
        } else {
            recordInDb.setLoses(recordInDb.getLoses() + 1);
            scoreboardRepo.save(recordInDb);
        }
    }

}
