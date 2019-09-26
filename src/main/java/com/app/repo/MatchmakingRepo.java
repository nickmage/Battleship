package com.app.repo;

import com.app.entities.Matchmaking;
import com.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchmakingRepo extends JpaRepository<Matchmaking, Long> {

    List<Matchmaking> findByPlayer1Name(String player1Name);

    List<Matchmaking> findByPlayer2Name(String player2Name);

    Matchmaking findByRoomId(String roomId);

}
