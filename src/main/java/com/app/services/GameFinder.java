package com.app.services;

import com.app.entities.Game;
import com.app.cache.Room;
import com.app.cache.RoomCache;
import com.app.repo.GameRepo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class GameFinder {

    private final GameRestorer gameRestorer;
    private final GameRepo gameRepo;

    public GameFinder(GameRestorer gameRestorer, GameRepo gameRepo) {
        this.gameRestorer = gameRestorer;
        this.gameRepo = gameRepo;
    }

    public Room findGame(String roomId) throws IOException {
        Room room = findGameInCache(roomId);
        return room == null ? findGameInDataBase(roomId) : room;
    }

    private Room findGameInCache(String roomId) {
        return RoomCache.rooms.get(roomId);
    }

    private Room findGameInDataBase(String roomId) throws IOException {
        Game game = gameRepo.findByRoomId(UUID.fromString(roomId));
        return game == null ? null : gameRestorer.restore(game);
    }

}