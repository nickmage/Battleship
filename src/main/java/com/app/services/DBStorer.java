package com.app.services;

import com.app.entities.Game;
import com.app.entities.Shot;
import com.app.models.BoardCell;
import com.app.repo.GameRepo;
import com.app.repo.ShotRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class DBStorer {

    private final GameRepo gameRepo;
    private final ShotRepo shotRepo;
    private final ObjectMapper objectMapper;

    public DBStorer(GameRepo gameRepo, ShotRepo shotRepo, ObjectMapper objectMapper) {
        this.gameRepo = gameRepo;
        this.shotRepo = shotRepo;
        this.objectMapper = objectMapper;
    }


    void storeShotToDB(UUID roomId, UUID playerId, int x, int y, int value){
        Shot shot = new Shot();
        shot.setRoomId(roomId);
        shot.setPlayerId(playerId);
        shot.setX(x);
        shot.setY(y);
        shot.setValue(value);
        shotRepo.save(shot);
    }

   void storeGameToDB(UUID roomId, ArrayList<ArrayList<BoardCell>> ships, int currentPlayer, boolean hit) throws JsonProcessingException {
        Game game = gameRepo.findByRoomId(roomId);
        if (currentPlayer == 1){
            game.setPlayer2Ships(objectMapper.writeValueAsString(ships));
        } else {
            game.setPlayer1Ships(objectMapper.writeValueAsString(ships));
        }
        if (!hit) {
            currentPlayer = currentPlayer == 1 ? 2 : 1;
        }
        game.setCurrentPlayer(currentPlayer);
        gameRepo.save(game);
    }

}
