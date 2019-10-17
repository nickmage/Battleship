package com.app.services;

import com.app.cache.Room;
import com.app.cache.RoomCache;
import com.app.entities.Match;
import com.app.entities.PrivateMatch;
import com.app.models.BoardCell;
import com.app.models.Ship;
import com.app.repo.GameRepo;
import com.app.repo.MatchRepo;
import com.app.repo.PrivateMatchRepo;
import com.app.response_wrappers.StartResponseWrapper;
import com.auth.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MatchSaver {

    private final MatchCreator matchCreator;
    private final GameRepo gameRepo;
    private final GameCreator gameCreator;
    private final MatchRepo matchRepo;
    private final PrivateMatchRepo privateMatchRepo;
    private final ObjectMapper objectMapper;
    private final BoardCreator boardCreator;

    public MatchSaver(MatchCreator matchCreator, GameRepo gameRepo, GameCreator gameCreator, MatchRepo matchRepo, PrivateMatchRepo privateMatchRepo, ObjectMapper objectMapper, BoardCreator boardCreator) {
        this.matchCreator = matchCreator;
        this.gameRepo = gameRepo;
        this.gameCreator = gameCreator;
        this.matchRepo = matchRepo;
        this.privateMatchRepo = privateMatchRepo;
        this.objectMapper = objectMapper;
        this.boardCreator = boardCreator;
    }

    public ResponseEntity getResponse(Ship[] ships, User userFromDB) throws JsonProcessingException {
        UUID playerId = userFromDB.getId();
        List<Match> freeRooms = matchRepo.findByPlayer2Name(null);
        //remove rooms to avoid playing with himself
        freeRooms.removeIf(match -> match.getPlayer1Id().equals(playerId));
        if (freeRooms.size() == 0) {
            Match match = matchCreator.createNewRoom(userFromDB, ships);
            matchRepo.save(match);
            return new ResponseEntity<>(new StartResponseWrapper(match.getPlayer1Id().toString(),
                    match.getRoomId().toString()), HttpStatus.OK);
        } else {
            Match match = matchCreator.joinExistingRoom(freeRooms.get(0), userFromDB, ships);
            matchRepo.save(match);
            gameRepo.save(gameCreator.createNewGame(match));
            return new ResponseEntity<>(new StartResponseWrapper(match.getPlayer2Id().toString(),
                    match.getRoomId().toString()), HttpStatus.OK);
        }
    }

    public ResponseEntity getResponse(Ship[] shipStartCells, User user, String roomId) throws JsonProcessingException {
        PrivateMatch match = privateMatchRepo.findByRoomId(UUID.fromString(roomId));
        ArrayList<ArrayList<BoardCell>> ships = boardCreator.getShips(shipStartCells);
        if (match.getPlayer1Name().equals(user.getUsername())) {
            match.setPlayer1Ships(objectMapper.writeValueAsString(ships));
            privateMatchRepo.save(match);
            cachePlayer1Data(RoomCache.rooms.get(roomId), ships, shipStartCells);
            if (match.getPlayer2Ships() != null) {
                gameRepo.save(gameCreator.createNewGame(match));
            }
            return new ResponseEntity<>(new StartResponseWrapper(match.getPlayer1Id().toString(),
                    match.getRoomId().toString()), HttpStatus.OK);
        } else {
            match.setPlayer2Id(user.getId());
            match.setPlayer2Ships(objectMapper.writeValueAsString(ships));
            privateMatchRepo.save(match);
            cachePlayer2Data(match, RoomCache.rooms.get(roomId), ships, shipStartCells);
            if (match.getPlayer1Ships() != null) {
                gameRepo.save(gameCreator.createNewGame(match));
            }
            return new ResponseEntity<>(new StartResponseWrapper(match.getPlayer2Id().toString(),
                    match.getRoomId().toString()), HttpStatus.OK);
        }
    }

    private void cachePlayer1Data(Room room, ArrayList<ArrayList<BoardCell>> ships, Ship[] shipStartCells){
        room.setPlayer1Ships(ships);
        room.setPlayer1Board(boardCreator.getBoard(shipStartCells));
    }

    private void cachePlayer2Data(PrivateMatch match, Room room, ArrayList<ArrayList<BoardCell>> ships, Ship[] shipStartCells){
        room.setPlayer2Name(match.getPlayer2Name());
        room.setPlayer2Id(match.getPlayer2Id());
        room.setPlayer2Ships(ships);
        room.setPlayer2Board(boardCreator.getBoard(shipStartCells));
    }

}
