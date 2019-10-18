package com.app.repo;

import com.app.entities.Scoreboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScoreboardRepo extends JpaRepository<Scoreboard, UUID> {

    List<Scoreboard> findTop10ByOrderByWinsDesc();

}
