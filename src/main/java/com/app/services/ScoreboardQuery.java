package com.app.services;

import com.app.entities.Scoreboard;
import com.app.repo.ScoreboardRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreboardQuery {

    private final ScoreboardRepo scoreboardRepo;

    public ScoreboardQuery(ScoreboardRepo scoreboardRepo) {
        this.scoreboardRepo = scoreboardRepo;
    }

    public List<Scoreboard> getScoreboard(){
        return scoreboardRepo.findTop10ByOrderByWinsDesc();
    }
}
