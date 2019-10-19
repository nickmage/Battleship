package com.app.repo;

import com.app.entities.Game;
import com.app.entities.GameType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GameRepo extends JpaRepository<Game, UUID> {

    Game findByRoomId(UUID roomId);

    Game findByRoomIdAndTypeEquals(UUID roomId, GameType type);

    List<Game> findByPlayer2NameIsNullAndPlayer1NameNotAndTypeEquals(String playerName, GameType type);

    Game findByRoomNameAndPlayer2IdIsNull(String roomName);
}
