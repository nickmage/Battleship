package com.app.controllers;

import com.app.DTOs.GameDTO;
import com.app.cache.Room;
import com.app.cache.RoomCache;
import com.app.repo.GameRepo;
import com.app.response_wrappers.GameInitResponseWrapper;
import com.app.services.GameFinder;
import com.app.services.GameInitResponseCreator;
import com.app.services.TurnMaker;
import com.app.validation.TurnValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameRepo gameRepo;
    private final TurnMaker turnMaker;
    private final TurnValidator turnValidator;
    private final GameFinder gameFinder;
    private final GameInitResponseCreator responseCreator;

    public GameController(GameRepo gameRepo, TurnMaker turnMaker, TurnValidator turnValidator,
                          GameFinder gameFinder, GameInitResponseCreator responseCreator) {
        this.gameRepo = gameRepo;
        this.turnMaker = turnMaker;
        this.turnValidator = turnValidator;
        this.gameFinder = gameFinder;
        this.responseCreator = responseCreator;
    }

    @GetMapping("/init")
    public ResponseEntity initGame(@RequestParam(name = "roomId") String roomId,
                                   @RequestParam(name = "playerId") String playerId) throws IOException {
        if (roomId != null && playerId != null && !roomId.equals("null") && !playerId.equals("null")){
            Room room = gameFinder.findGame(roomId);
            if (room != null){
                RoomCache.rooms.put(roomId, room);
                GameInitResponseWrapper response = responseCreator.getResponse(room, playerId);
                return ResponseEntity.ok(response);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/shot")
    public ResponseEntity makeShot(@RequestParam(name = "roomId") String roomId,
                                   @RequestParam(name = "playerId") String playerId,
                                   @RequestParam(name = "x") int x,
                                   @RequestParam(name = "y") int y) throws JsonProcessingException {
        if (roomId != null && playerId != null && !roomId.equals("null") && !playerId.equals("null")){
            Room room = RoomCache.rooms.get(roomId);
            if (turnValidator.isValidTurn(room, x, y, playerId)){
                return ResponseEntity.ok(turnMaker.makeShot(room, x, y));
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

/*request every 30 sec to find out another player left*/
}