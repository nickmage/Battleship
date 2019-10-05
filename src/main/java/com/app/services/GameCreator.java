package com.app.services;

import com.app.DTOs.GameDTO;
import com.app.DTOs.MatchDTO;

import com.app.cache.RoomCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class GameCreator {
    private final MatchDTO matchDTO;
    private final GameDTO gameDTO;
    private final ObjectMapper objectMapper;

    public GameCreator(MatchDTO matchDTO, GameDTO gameDTO, ObjectMapper objectMapper) {
        this.matchDTO = matchDTO;
        this.gameDTO = gameDTO;
        this.objectMapper = objectMapper;
    }

    public GameDTO createNewGame(MatchDTO matchDTO) {
        gameDTO.setRoomId(matchDTO.getRoomId());
        gameDTO.setCurrentPlayer(new Random().nextInt(2) + 1);
        gameDTO.setPlayer1Name(matchDTO.getPlayer1Name());
        gameDTO.setPlayer1Id(matchDTO.getPlayer1Id());
        gameDTO.setPlayer2Name(matchDTO.getPlayer2Name());
        gameDTO.setPlayer2Id(matchDTO.getPlayer2Id());
        gameDTO.setDate(new Date());
        gameDTO.setPlayer1Ships(matchDTO.getPlayer1Ships());
        gameDTO.setPlayer2Ships(matchDTO.getPlayer2Ships());
        gameDTO.setWinner(0);
        RoomCache.rooms.get(matchDTO.getRoomId().toString()).setCurrentPlayer(gameDTO.getCurrentPlayer());
        return gameDTO;
    }

}
