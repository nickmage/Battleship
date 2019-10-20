package com.app.controllers;

import com.app.entities.Game;
import com.app.entities.GameType;
import com.app.entities.Scoreboard;
import com.app.models.Ship;
import com.app.repo.GameRepo;
import com.app.repo.ScoreboardRepo;
import com.app.response_wrappers.RoomListResponseWrapper;
import com.app.response_wrappers.ScoreboardWrapper;
import com.app.services.GameCreator;
import com.app.validation.BoardValidator;
import com.auth.entities.User;
import com.auth.repo.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class PreparationController {

    private final UserRepo userRepo;
    private final GameRepo gameRepo;
    private final ScoreboardRepo scoreboardRepo;
    private final GameCreator gameCreator;

    public PreparationController(UserRepo userRepo, GameRepo gameRepo, ScoreboardRepo scoreboardRepo,
                                 GameCreator gameCreator) {
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
        this.scoreboardRepo = scoreboardRepo;
        this.gameCreator = gameCreator;
    }

    @PostMapping("/start")
    public ResponseEntity getBoard(@RequestBody Ship[] ships,
                                   @RequestHeader(name = "Username") String username,
                                   @RequestHeader(name = "RoomId", required = false) String roomId) throws IOException {
        User userFromDB = userRepo.findByUsername(username);
        if (userFromDB != null) {
            if (new BoardValidator(ships).isValidBoard()) {
                if (roomId == null) {
                    return gameCreator.getResponseForPublicGame(ships, userFromDB);
                } else {
                    return gameCreator.getResponseForPrivateGame(ships, userFromDB, roomId);
                }
            } else {
                return new ResponseEntity<>("An error has occurred (the board is invalid)! Please try again and send a valid board.",
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Cannot find your account! Please reenter the game and try again!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/lobby")
    public ResponseEntity lobby(@RequestParam(name = "roomId") String roomId) {
        Game game = gameRepo.findByRoomIdAndTypeEquals(UUID.fromString(roomId), GameType.PUBLIC);
        if (game == null || game.getPlayer2Name() == null) {
            Game privateGame = gameRepo.findByRoomIdAndTypeEquals(UUID.fromString(roomId), GameType.PRIVATE);
            if (privateGame == null || privateGame.getPlayer1Ships() == null || privateGame.getPlayer2Ships() == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity(HttpStatus.OK);
            }
        } else {
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @GetMapping("/scoreboard")
    public ResponseEntity<List<ScoreboardWrapper>> scoreboard() {
        List<Scoreboard> records = scoreboardRepo.findTop10ByOrderByWinsDesc();
        List<ScoreboardWrapper> response = new ArrayList<>();
        for (Scoreboard record : records) {
            ScoreboardWrapper wrapper = new ScoreboardWrapper(record.getUser().getUsername(),
                    record.getWins(), record.getLoses());
            response.add(wrapper);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/room")
    public ResponseEntity createRoom(@RequestParam(name = "roomName") String roomName,
                                     @RequestParam(name = "password") String password,
                                     @RequestParam(name = "username") String username) {
        if (roomName == null || roomName.equals("null") || password == null || password.equals("null") ||
                username == null || username.equals("null") || roomName.length() == 0 || password.length() == 0 ||
                username.length() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Game gameFromDB = gameRepo.findByRoomNameAndPlayer2IdIsNull(roomName);
            if (gameFromDB == null) {
                return gameCreator.createPrivateGame(roomName, password, username);
            } else return new ResponseEntity<>("The room with this name already exists.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/roomlist")
    public ResponseEntity<List<RoomListResponseWrapper>> roomList(@RequestParam(name = "playerName") String playerName) {
        List<Game> gamesFromDB = gameRepo.findFreeGamesWithType(playerName, GameType.PRIVATE);
        List<RoomListResponseWrapper> response = new ArrayList<>();
        for (Game game : gamesFromDB) {
            response.add(new RoomListResponseWrapper(game.getRoomId(), game.getRoomName(), game.getPlayer1Name()));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/roomlist")
    public ResponseEntity roomEnterPermission(@RequestParam(name = "password") String password,
                                              @RequestParam(name = "username") String username,
                                              @RequestParam(name = "roomId") String roomId) {
        if (roomId == null || roomId.equals("null") || password == null || password.equals("null") ||
                username == null || username.equals("null") || roomId.length() == 0 || password.length() == 0 ||
                username.length() == 0) {
            return new ResponseEntity<>("Incorrect data", HttpStatus.BAD_REQUEST);
        } else {
            Game game = gameRepo.findByRoomIdAndTypeEquals(UUID.fromString(roomId), GameType.PRIVATE);
            if (game == null) {
                return new ResponseEntity<>("Room not found", HttpStatus.BAD_REQUEST);
            } else if (game.getPlayer2Name() != null) {
                return new ResponseEntity<>("This room has been already occupied", HttpStatus.BAD_REQUEST);
            } else if (!game.getPassword().equals(password)) {
                return new ResponseEntity<>("Password is incorrect", HttpStatus.BAD_REQUEST);
            } else {
                game.setPlayer2Name(username);
                gameRepo.save(game);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }

}
