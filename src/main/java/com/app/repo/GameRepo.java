package com.app.repo;

import com.app.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepo extends JpaRepository<Game, Long> {

    Game findByRoomId(UUID roomId);

    Game findFirstByRoomIdOrderByIdDesc(UUID roomId);

}
