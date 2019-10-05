package com.app.repo;

import com.app.DTOs.GameDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepo extends JpaRepository<GameDTO, Long> {

    GameDTO findByRoomId(UUID roomId);

    GameDTO findFirstByRoomIdOrderByIdDesc(UUID roomId);

}
