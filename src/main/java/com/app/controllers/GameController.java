package com.app.controllers;

import com.app.DTOs.Game;
import com.app.repo.GameRepo;
import com.app.response_wrappers.GameInitResponseWrapper;
import com.app.services.TurnMaker;
import com.app.validation.TurnValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;


@RestController
public class GameController {
    @Autowired
    private GameRepo gameRepo;
    @Autowired
    private GameInitResponseWrapper gameInitResponseWrapper;
    @Autowired
    private TurnMaker turnMaker;
    @Autowired
    private TurnValidator turnValidator;

    @GetMapping("/game/init")
    @ResponseBody
    public ResponseEntity initGame(@RequestParam(name = "roomId") String roomId, @RequestParam(name = "playerId") String playerId) {
        if (roomId != null && playerId != null){
            System.out.println(roomId.length());
            Game game = gameRepo.findByRoomId(UUID.fromString(roomId));
            if (game != null){
                if (UUID.fromString(playerId).equals(game.getPlayer1Id())){
                    gameInitResponseWrapper.setPlayerBoard(game.getPlayer1BoardJSON());
                    gameInitResponseWrapper.setPlayerShips(game.getRemainingShipsOfPlayer1JSON());
                    gameInitResponseWrapper.setEnemyShips(game.getRemainingShipsOfPlayer2JSON());
                    gameInitResponseWrapper.setEnemyName(game.getPlayer2Name());
                    gameInitResponseWrapper.setMyTurn(game.getCurrentPlayer() == 1);
                } else {
                    gameInitResponseWrapper.setPlayerBoard(game.getPlayer2BoardJSON());
                    gameInitResponseWrapper.setPlayerShips(game.getRemainingShipsOfPlayer2JSON());
                    gameInitResponseWrapper.setEnemyShips(game.getRemainingShipsOfPlayer1JSON());
                    gameInitResponseWrapper.setEnemyName(game.getPlayer1Name());
                    gameInitResponseWrapper.setMyTurn(game.getCurrentPlayer() == 2);
                }
                return ResponseEntity.ok(gameInitResponseWrapper);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/game/shot")
    @ResponseBody
    public ResponseEntity makeShot(@RequestParam(name = "roomId") String roomId,
                                   @RequestParam(name = "playerId") String playerId,
                                   @RequestParam(name = "x") int x,
                                   @RequestParam(name = "y") int y) throws IOException {
        if (roomId != null && playerId != null && !roomId.equals("null")){
            Game game = gameRepo.findFirstByRoomIdOrderByIdDesc(UUID.fromString(roomId));
            //refresh null board & edit enemy own one & edit ship (invert value) in matchmaking
            //return result -> hit or miss

            if (turnValidator.isValidTurn(game, x, y, playerId)){
                //return ResponseEntity.ok(turnValidator.isValidTurn(game, x, y, playerId));
                return ResponseEntity.ok(turnMaker.makeShot(game, x, y));
            } else {
                //return ResponseEntity.ok(turnValidator.isValidTurn(game, x, y, playerId));
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
/*request every 30 sec to find out another player left*/
}