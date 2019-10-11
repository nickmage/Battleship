package com.app.services;

import com.app.entities.Match;
import com.app.cache.Room;
import com.app.cache.RoomCache;
import com.app.models.BoardCell;
import com.app.models.Ship;
import com.auth.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class MatchCreator {
    private final Match match;
    private final ObjectMapper objectMapper;
    private final BoardCreator boardCreator;

    public MatchCreator(Match match, ObjectMapper objectMapper, BoardCreator boardCreator) {
        this.match = match;
        this.objectMapper = objectMapper;
        this.boardCreator = boardCreator;
    }

    public Match createNewRoom(User user, Ship[] shipStartCells) throws JsonProcessingException {
        match.setPlayer1Name(user.getUsername());
        match.setPlayer1Id(user.getUuid());
        match.setRoomId(UUID.randomUUID());
        match.setDate(new Date());
        ArrayList<ArrayList<BoardCell>> ships = boardCreator.getShips(shipStartCells);
        match.setPlayer1Ships(objectMapper.writeValueAsString(ships));
        cacheNewRoom(ships, shipStartCells, match.getRoomId());
        return match;
    }

    public Match joinExistingRoom(Match match, User user, Ship[] shipStartCells) throws JsonProcessingException {
        match.setPlayer2Name(user.getUsername());
        match.setPlayer2Id(user.getUuid());
        ArrayList<ArrayList<BoardCell>> ships = boardCreator.getShips(shipStartCells);
        match.setPlayer2Ships(objectMapper.writeValueAsString(ships));
        cacheExistingRoom(match, ships, shipStartCells);
        return match;
    }

    private void cacheNewRoom(ArrayList<ArrayList<BoardCell>> ships, Ship[] shipStartCells, UUID roomId) {
        Room room = new Room();
        room.setRoomId(roomId);
        room.setPlayer1Name(match.getPlayer1Name());
        room.setPlayer1Id(match.getPlayer1Id());
        room.setPlayer1Board(boardCreator.getBoard(shipStartCells));
        room.setPlayer1Ships(ships);
        RoomCache.rooms.put(match.getRoomId().toString(), room);
    }

    private void cacheExistingRoom(Match match, ArrayList<ArrayList<BoardCell>> ships, Ship[] shipStartCells) {
        Room room = RoomCache.rooms.get(match.getRoomId().toString());
        room.setPlayer2Name(match.getPlayer2Name());
        room.setPlayer2Id(match.getPlayer2Id());
        room.setPlayer2Board(boardCreator.getBoard(shipStartCells));
        room.setPlayer2Ships(ships);
    }

}