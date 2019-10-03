package com.app.repo;

import com.app.DTOs.GameDT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepo extends JpaRepository<GameDT, Long> {

    GameDT findByRoomId(UUID roomId);

    GameDT findFirstByRoomIdOrderByIdDesc(UUID roomId);

}
