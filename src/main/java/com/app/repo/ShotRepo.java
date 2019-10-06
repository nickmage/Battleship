package com.app.repo;

import com.app.DTOs.ShotDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShotRepo extends JpaRepository<ShotDTO, Long> {

    List<ShotDTO> findByRoomId(UUID roomId);

    List<ShotDTO> findByRoomIdAndPlayerIdAndValueEquals(UUID roomId, UUID playerId, Integer value);


}
