package com.app.repo;

import com.app.entities.Scoreboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreboardRepo extends JpaRepository<Scoreboard, Long> {

    List<Scoreboard> findTop10ByOrderByWinsDesc();

    Scoreboard findByUsername(String username);

}
