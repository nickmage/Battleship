package com.app.repo;

import com.app.entities.PrivateMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrivateMatchRepo extends JpaRepository<PrivateMatch, Long> {

    PrivateMatch findByRoomId(UUID roomId);

    PrivateMatch findByRoomNameAndPlayer2IdIsNull(String roomName);

    List<PrivateMatch> findAllByPlayer2IdIsNullAndPlayer2NameIsNullAndPlayer1NameNot(String playerName);

}
