package com.app.repo;

import com.app.DTOs.Matchmaking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatchmakingRepo extends JpaRepository<Matchmaking, Long> {

    List<Matchmaking> findByPlayer1Name(String player1Name);

    List<Matchmaking> findByPlayer2Name(String player2Name);

    Matchmaking findByRoomId(UUID roomId);

}
