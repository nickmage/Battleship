package com.app.services;

import com.app.DTOs.Game;
import com.app.DTOs.Match;
import com.app.entities.Ship;
import com.auth.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class MatchCreator {
    private final Match match;
    private final ObjectMapper objectMapper;
    private final BoardCreator boardCreator;

    public MatchCreator(Match match, Game game, ObjectMapper objectMapper, BoardCreator boardCreator) {
        this.match = match;
        this.objectMapper = objectMapper;
        this.boardCreator = boardCreator;
    }

    public Match createNewRoom(User user, Ship[] ships) throws JsonProcessingException {
        match.setPlayer1Name(user.getUsername());
        match.setPlayer1Id(user.getUuid());
        match.setRoomId(UUID.randomUUID());
        match.setDate(new Date());
        match.setPlayer1Ships(objectMapper.writeValueAsString(boardCreator.getShips(ships)));
        return match;
    }

    public Match joinExistingRoom(Match match, User user, Ship[] ships) throws JsonProcessingException {
        match.setPlayer2Name(user.getUsername());
        match.setPlayer2Id(user.getUuid());
        match.setPlayer2Ships(objectMapper.writeValueAsString(boardCreator.getShips(ships)));
        return match;
    }

}