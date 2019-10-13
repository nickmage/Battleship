package com.app.repo;

import com.app.entities.PrivateMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrivateMatchRepo extends JpaRepository<PrivateMatch, Long> {

    PrivateMatch findByRoomId(UUID roomId);

    PrivateMatch findByRoomNameAndPlayer2IdEquals(String roomName, String player2Id);

}
