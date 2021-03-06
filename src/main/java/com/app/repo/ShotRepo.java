package com.app.repo;

import com.app.entities.Shot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShotRepo extends JpaRepository<Shot, Long> {

    List<Shot> findByRoomIdAndPlayerIdAndValueEquals(UUID roomId, UUID playerId, Integer value);

    List<Shot> findByRoomIdAndPlayerId(UUID roomId, UUID playerId);

}
