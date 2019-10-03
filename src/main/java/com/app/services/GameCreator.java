package com.app.services;

import com.app.DTOs.GameDT;
import com.app.DTOs.MatchDTO;

import com.app.cache.RoomCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class GameCreator {
    private final MatchDTO matchDTO;
    private final GameDT gameDTO;
    private final ObjectMapper objectMapper;

    public GameCreator(MatchDTO matchDTO, GameDT gameDTO, ObjectMapper objectMapper) {
        this.matchDTO = matchDTO;
        this.gameDTO = gameDTO;
        this.objectMapper = objectMapper;
    }

    public GameDT createNewGame(MatchDTO matchDTO) {
        gameDTO.setRoomId(matchDTO.getRoomId());
        gameDTO.setCurrentPlayer(new Random().nextInt(2) + 1);
        gameDTO.setPlayer1Name(matchDTO.getPlayer1Name());
        gameDTO.setPlayer1Id(matchDTO.getPlayer1Id());
        gameDTO.setPlayer2Name(matchDTO.getPlayer2Name());
        gameDTO.setPlayer2Id(matchDTO.getPlayer2Id());
        gameDTO.setDate(new Date());
        gameDTO.setPlayer1Board(matchDTO.getPlayer1Ships());
        gameDTO.setPlayer2Board(matchDTO.getPlayer2Ships());
        gameDTO.setWinner(0);
        RoomCache.rooms.get(matchDTO.getRoomId().toString()).setCurrentPlayer(gameDTO.getCurrentPlayer());
        return gameDTO;
    }

}
