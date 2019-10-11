package com.app.repo;

import com.app.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatchRepo extends JpaRepository<Match, Long> {

    List<Match> findByPlayer1Name(String player1Name);

    List<Match> findByPlayer2Name(String player2Name);

    Match findByRoomId(UUID roomId);

}
