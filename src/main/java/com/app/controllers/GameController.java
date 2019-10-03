package com.app.controllers;

import com.app.DTOs.GameDT;
import com.app.cache.Room;
import com.app.repo.GameRepo;
import com.app.response_wrappers.GameInitResponseWrapper;
import com.app.services.GameFinder;
import com.app.services.TurnMaker;
import com.app.validation.TurnValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/game")
public class GameController {

    private final GameRepo gameRepo;
    private final TurnMaker turnMaker;
    private final TurnValidator turnValidator;
    private final GameFinder gameFinder;

    public GameController(GameRepo gameRepo, TurnMaker turnMaker, TurnValidator turnValidator, GameFinder gameFinder) {
        this.gameRepo = gameRepo;
        this.turnMaker = turnMaker;
        this.turnValidator = turnValidator;
        this.gameFinder = gameFinder;
    }


    @GetMapping("/init")
    public ResponseEntity initGame(@RequestParam(name = "roomId") String roomId, @RequestParam(name = "playerId") String playerId) {
        if (roomId != null && playerId != null){
            Room room = gameFinder.findGameInCache(roomId);
            if (room != null){
                GameInitResponseWrapper response = new GameInitResponseWrapper();
                if (playerId.equals(room.getPlayer1Id().toString())){
                    response.setPlayerBoard(room.getPlayer1Board());
                    response.setPlayerShips(room.getRemainingShipsOfPlayer1());
                    response.setEnemyShips(room.getRemainingShipsOfPlayer2());
                    if (room.getEnemyBoardForPlayer1() != null){
                        response.setEnemyBoard(room.getEnemyBoardForPlayer1());
                    }
                    response.setEnemyName(room.getPlayer2Name());
                    response.setMyTurn(room.getCurrentPlayer() == 1);
                } else {
                    response.setPlayerBoard(room.getPlayer2Board());
                    response.setPlayerShips(room.getRemainingShipsOfPlayer2());
                    response.setEnemyShips(room.getRemainingShipsOfPlayer1());
                    if (room.getEnemyBoardForPlayer2() != null){
                        response.setEnemyBoard(room.getEnemyBoardForPlayer2());
                    }
                    response.setEnemyName(room.getPlayer1Name());
                    response.setMyTurn(room.getCurrentPlayer() == 2);
                }
                response.setWinner(room.getWinner());
                return ResponseEntity.ok(response);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/shot")
    @ResponseBody
    public ResponseEntity makeShot(@RequestParam(name = "roomId") String roomId,
                                   @RequestParam(name = "playerId") String playerId,
                                   @RequestParam(name = "x") int x,
                                   @RequestParam(name = "y") int y) throws IOException {
        if (roomId != null && playerId != null && !roomId.equals("null")){
            GameDT gameDTO = gameRepo.findFirstByRoomIdOrderByIdDesc(UUID.fromString(roomId));
            if (turnValidator.isValidTurn(gameDTO, x, y, playerId)){
                return ResponseEntity.ok(turnMaker.makeShot(gameDTO, x, y));
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
/*request every 30 sec to find out another player left*/
}