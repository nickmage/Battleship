package com.app.services;

import com.app.DTOs.Game;
import com.app.DTOs.Matchmaking;
import com.app.entities.RemainingShips;
import com.app.entities.Ship;
import com.auth.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class MatchCreator {
    @Autowired
    private Matchmaking match;
    @Autowired
    private Game game;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BoardCreator boardCreator;

    public Matchmaking createNewRoom(User user, Ship[] ships) throws JsonProcessingException {
        match.setPlayer1Name(user.getUsername());
        match.setPlayer1Id(user.getUuid());
        match.setRoomId(UUID.randomUUID());
        match.setDate(new Date());
        match.setPlayer1BoardJSON(objectMapper.writeValueAsString(boardCreator.getBoard(ships)));
        match.setShipsOfPlayer1JSON(objectMapper.writeValueAsString(boardCreator.getShipsList(ships)));
        return match;
    }

    public Matchmaking joinExistingRoom(Matchmaking match, User user, Ship[] ships) throws JsonProcessingException {
        match.setPlayer2Name(user.getUsername());
        match.setPlayer2Id(user.getUuid());
        match.setPlayer2BoardJSON(objectMapper.writeValueAsString(boardCreator.getBoard(ships)));
        match.setShipsOfPlayer2JSON(objectMapper.writeValueAsString(boardCreator.getShipsList(ships)));
        return match;
    }

    public Game createNewGame(Matchmaking match) throws IOException {
        game.setRoomId(match.getRoomId());
        game.setCurrentPlayer(new Random().nextInt(2) + 1);
        game.setPlayer1Name(match.getPlayer1Name());
        game.setPlayer1Id(match.getPlayer1Id());
        game.setPlayer2Name(match.getPlayer2Name());
        game.setPlayer2Id(match.getPlayer2Id());
        game.setDate(new Date());
        game.setPlayer1BoardJSON(match.getPlayer1BoardJSON());
        game.setPlayer2BoardJSON(match.getPlayer2BoardJSON());
        game.setShipsOfPlayer1JSON(match.getShipsOfPlayer1JSON());
        game.setShipsOfPlayer2JSON(match.getShipsOfPlayer2JSON());
        game.setRemainingShipsOfPlayer1JSON(objectMapper.writeValueAsString(new RemainingShips()));
        game.setRemainingShipsOfPlayer2JSON(objectMapper.writeValueAsString(new RemainingShips()));
        game.setWinner(0);
        return game;
    }


}