package com.app.services;

import com.app.DTOs.Game;
import com.app.DTOs.Match;
import com.app.entities.Room;
import com.app.entities.RoomCash;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

@Service
public class GameCreator {
    private final Match match;
    private final Game game;
    private final ObjectMapper objectMapper;

    public GameCreator(Match match, Game game, ObjectMapper objectMapper) {
        this.match = match;
        this.game = game;
        this.objectMapper = objectMapper;
    }


    public Game createNewGame(Match match) throws IOException {
        game.setRoomId(match.getRoomId());
        game.setCurrentPlayer(new Random().nextInt(2) + 1);
        game.setPlayer1Name(match.getPlayer1Name());
        game.setPlayer1Id(match.getPlayer1Id());
        game.setPlayer2Name(match.getPlayer2Name());
        game.setPlayer2Id(match.getPlayer2Id());
        game.setDate(new Date());
        game.setPlayer1Board(match.getPlayer1Ships());
        game.setPlayer1Board(match.getPlayer1Ships());
        game.setWinner(0);
        //cashNewRoom(game);
        return game;
    }

    private void cashNewRoom(Game game){
        Room room = new Room();
        room.setCurrentPlayer(game.getCurrentPlayer());
        room.setPlayer1Name(match.getPlayer1Name());
        room.setPlayer1Id(match.getPlayer1Id());
        room.setPlayer2Name(match.getPlayer2Name());
        room.setPlayer2Id(match.getPlayer2Id());
        room.setDate(new Date());

        //room.setPlayer1Board(match.getPlayer1Ships());
        //room.setPlayer1Board(match.getPlayer1Ships());
        room.setWinner(0);
        RoomCash.rooms.put(game.getRoomId().toString(), room);
    }
}
