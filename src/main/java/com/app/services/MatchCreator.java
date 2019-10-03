package com.app.services;

import com.app.DTOs.MatchDTO;
import com.app.cache.Room;
import com.app.cache.RoomCache;
import com.app.entities.BoardCell;
import com.app.entities.Ship;
import com.auth.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class MatchCreator {
    private final MatchDTO matchDTO;
    private final ObjectMapper objectMapper;
    private final BoardCreator boardCreator;

    public MatchCreator(MatchDTO matchDTO, ObjectMapper objectMapper, BoardCreator boardCreator) {
        this.matchDTO = matchDTO;
        this.objectMapper = objectMapper;
        this.boardCreator = boardCreator;
    }

    public MatchDTO createNewRoom(User user, Ship[] shipStartCells) throws JsonProcessingException {
        matchDTO.setPlayer1Name(user.getUsername());
        matchDTO.setPlayer1Id(user.getUuid());
        matchDTO.setRoomId(UUID.randomUUID());
        matchDTO.setDate(new Date());
        ArrayList<ArrayList<BoardCell>> ships = boardCreator.getShips(shipStartCells);
        matchDTO.setPlayer1Ships(objectMapper.writeValueAsString(ships));
        cacheNewRoom(ships, shipStartCells);
        return matchDTO;
    }

    public MatchDTO joinExistingRoom(MatchDTO matchDTO, User user, Ship[] shipStartCells) throws JsonProcessingException {
        matchDTO.setPlayer2Name(user.getUsername());
        matchDTO.setPlayer2Id(user.getUuid());
        ArrayList<ArrayList<BoardCell>> ships = boardCreator.getShips(shipStartCells);
        matchDTO.setPlayer2Ships(objectMapper.writeValueAsString(ships));
        cacheExistingRoom(matchDTO, ships, shipStartCells);
        return matchDTO;
    }

    private void cacheNewRoom(ArrayList<ArrayList<BoardCell>> ships, Ship[] shipStartCells) {
        Room room = new Room();
        room.setPlayer1Name(matchDTO.getPlayer1Name());
        room.setPlayer1Id(matchDTO.getPlayer1Id());
        room.setPlayer1Board(boardCreator.getBoard(shipStartCells));
        room.setShipsOfPlayer1(ships);
        RoomCache.rooms.put(matchDTO.getRoomId().toString(), room);
    }

    private void cacheExistingRoom(MatchDTO matchDTO,ArrayList<ArrayList<BoardCell>> ships, Ship[] shipStartCells) {
        Room room = RoomCache.rooms.get(matchDTO.getRoomId().toString());
        room.setPlayer2Name(matchDTO.getPlayer2Name());
        room.setPlayer2Id(matchDTO.getPlayer2Id());
        room.setPlayer2Board(boardCreator.getBoard(shipStartCells));
        room.setShipsOfPlayer2(ships);
    }

}