package com.app.repo;

import com.app.entities.Game;
import com.app.entities.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GameRepo extends JpaRepository<Game, UUID> {

    Game findByRoomId(UUID roomId);

    Game findByRoomIdAndTypeEquals(UUID roomId, GameType type);

    @Query("SELECT g FROM Game g WHERE g.player2Name IS NULL AND g.player1Name <> :playerName AND g.type= :type")
    List<Game> findFreeGamesWithType(String playerName, GameType type);

    Game findByRoomNameAndPlayer2IdIsNull(String roomName);

}
