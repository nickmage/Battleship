package com.app.services;

import com.app.entities.Game;
import com.app.entities.Match;

import com.app.cache.RoomCache;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class GameCreator {

    public Game createNewGame(Match match) {
        Game game = new Game();
        game.setRoomId(match.getRoomId());
        game.setCurrentPlayer(new Random().nextInt(2) + 1);
        game.setPlayer1Name(match.getPlayer1Name());
        game.setPlayer1Id(match.getPlayer1Id());
        game.setPlayer2Name(match.getPlayer2Name());
        game.setPlayer2Id(match.getPlayer2Id());
        game.setDate(new Date());
        game.setPlayer1Ships(match.getPlayer1Ships());
        game.setPlayer2Ships(match.getPlayer2Ships());
        game.setWinner(0);
        RoomCache.rooms.get(match.getRoomId().toString()).setCurrentPlayer(game.getCurrentPlayer());
        return game;
    }

}
