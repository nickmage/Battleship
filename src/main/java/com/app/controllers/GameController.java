package com.app.controllers;

import com.app.cache.Room;
import com.app.cache.RoomCache;
import com.app.entities.Game;
import com.app.repo.GameRepo;
import com.app.response_wrappers.GameInitResponseWrapper;
import com.app.response_wrappers.GameStatusResponseWrapper;
import com.app.services.*;
import com.app.validation.TurnValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {

    private final TurnMaker turnMaker;
    private final TurnValidator turnValidator;
    private final GameFinder gameFinder;
    private final GameInitResponseCreator initResponseCreator;
    private final GameStatusResponseCreator statusResponseCreator;
    private final ScoreboardSaver scoreboardSaver;
    private final GameRepo gameRepo;

    public GameController(TurnMaker turnMaker, TurnValidator turnValidator,
                          GameFinder gameFinder, GameInitResponseCreator initResponseCreator,
                          GameStatusResponseCreator statusResponseCreator, ScoreboardSaver scoreboardSaver, GameRepo gameRepo) {
        this.turnMaker = turnMaker;
        this.turnValidator = turnValidator;
        this.gameFinder = gameFinder;
        this.initResponseCreator = initResponseCreator;
        this.statusResponseCreator = statusResponseCreator;
        this.scoreboardSaver = scoreboardSaver;
        this.gameRepo = gameRepo;
    }

    @GetMapping("/init")
    public ResponseEntity initGame(@RequestParam(name = "roomId") String roomId,
                                   @RequestParam(name = "playerId") String playerId) throws IOException {
        if (roomId != null && playerId != null && !roomId.equals("null") && !playerId.equals("null")){
            Room room = gameFinder.findGame(roomId);
            if (room != null){
                RoomCache.rooms.put(roomId, room);
                GameInitResponseWrapper response = initResponseCreator.getResponse(room, playerId);
                return ResponseEntity.ok(response);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/shot")
    public ResponseEntity makeShot(@RequestParam(name = "roomId") String roomId,
                                   @RequestParam(name = "playerId") String playerId,
                                   @RequestParam(name = "x") int x,
                                   @RequestParam(name = "y") int y) throws IOException {
        if (roomId != null && playerId != null && !roomId.equals("null") && !playerId.equals("null")){
            Room room = gameFinder.findGame(roomId);
            if (turnValidator.isValidTurn(room, x, y, playerId)){
                return ResponseEntity.ok(turnMaker.makeShot(room, x, y));
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/status")
    public ResponseEntity gameStatus(@RequestParam(name = "roomId") String roomId,
                                     @RequestParam(name = "playerId") String playerId) throws IOException {
        if (roomId != null && playerId != null && !roomId.equals("null") && !playerId.equals("null")){
            Room room = gameFinder.findGame(roomId);
            if (room != null){
                RoomCache.rooms.put(roomId, room);
                GameStatusResponseWrapper response = statusResponseCreator.getResponse(room, playerId);
                return ResponseEntity.ok(response);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }






    @PostMapping("/surrender")
    public ResponseEntity surrender(@RequestParam(name = "roomId") String roomId,
                                    @RequestParam(name = "playerId") String playerId) throws IOException {
        System.out.println(roomId);
        System.out.println(playerId);
        /*Room room = gameFinder.findGame(roomId);
        int winner;
        if (room.getPlayer1Id().toString().equals(playerId)){
            winner = 2;
        } else if (room.getPlayer2Id().toString().equals(playerId)){
            winner = 1;
        } else return new ResponseEntity(HttpStatus.BAD_REQUEST);
        room.setWinner(winner);
        Game game = gameRepo.findByRoomId(UUID.fromString(roomId));
        game.setWinner(winner);
        gameRepo.save(game);*/
        return new ResponseEntity(HttpStatus.OK);
    }






    @PostMapping("/win")
    public ResponseEntity games(@RequestParam(name = "winner") String winner,
                                @RequestParam(name = "loser") String loser) {
        scoreboardSaver.storeScoreboard(winner, loser);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity deleteRoom(@RequestParam(name = "roomId") String roomId) {
        RoomCache.rooms.remove(roomId);
        return new ResponseEntity(HttpStatus.OK);
    }

}