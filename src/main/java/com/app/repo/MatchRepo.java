package com.app.repo;

import com.app.DTOs.MatchDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatchRepo extends JpaRepository<MatchDTO, Long> {

    List<MatchDTO> findByPlayer1Name(String player1Name);

    List<MatchDTO> findByPlayer2Name(String player2Name);

    MatchDTO findByRoomId(UUID roomId);

}
