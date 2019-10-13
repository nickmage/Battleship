package com.app.controllers;

import com.app.entities.Match;
import com.app.entities.PrivateMatch;
import com.app.entities.Scoreboard;
import com.app.models.Ship;
import com.app.repo.GameRepo;
import com.app.repo.MatchRepo;
import com.app.repo.PrivateMatchRepo;
import com.app.repo.ScoreboardRepo;
import com.app.response_wrappers.StartResponseWrapper;
import com.app.services.GameCreator;
import com.app.services.MatchCreator;
import com.app.services.MatchSaver;
import com.app.validation.BoardValidator;
import com.auth.entities.User;
import com.auth.repo.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class PreparationController {

    private final UserRepo userRepo;
    private final MatchRepo matchRepo;
    private final MatchCreator matchCreator;
    private final GameRepo gameRepo;
    private final GameCreator gameCreator;
    private final ScoreboardRepo scoreboardRepo;
    private final PrivateMatchRepo privateMatchRepo;
    private final MatchSaver matchSaver;

    public PreparationController(UserRepo userRepo, MatchRepo matchRepo, MatchCreator matchCreator,
                                 GameRepo gameRepo, GameCreator gameCreator, ScoreboardRepo scoreboardRepo, PrivateMatchRepo privateMatchRepo, MatchSaver matchSaver) {
        this.userRepo = userRepo;
        this.matchRepo = matchRepo;
        this.matchCreator = matchCreator;
        this.gameRepo = gameRepo;
        this.gameCreator = gameCreator;
        this.scoreboardRepo = scoreboardRepo;
        this.privateMatchRepo = privateMatchRepo;
        this.matchSaver = matchSaver;
    }

    @PostMapping("/start")
    public ResponseEntity getBoard(@RequestBody Ship[] ships,
                                   @RequestHeader(name = "Username") String username,
                                   @RequestHeader(name = "RoomId", required = false) String roomId) throws IOException {
        System.out.println(roomId);
        //return matchSaver.getResponse(ships, username);
        if (new BoardValidator(ships).isValidBoard()) {
            User userFromDB = userRepo.findByUsername(username);
            if (userFromDB != null) {
                UUID playerId = userFromDB.getUuid();
                List<Match> freeRooms = matchRepo.findByPlayer2Name(null);
                //remove rooms to avoid playing with himself
                freeRooms.removeIf(match -> match.getPlayer1Id().equals(playerId));
                if (freeRooms.size() == 0) {
                    Match match = matchCreator.createNewRoom(userFromDB, ships);
                    matchRepo.save(match);
                    return new ResponseEntity<>(new StartResponseWrapper(match.getPlayer1Id().toString(),
                            match.getRoomId().toString()), HttpStatus.OK);
                } else {
                    Match match = matchCreator.joinExistingRoom(freeRooms.get(0), userFromDB, ships);
                    matchRepo.save(match);
                    gameRepo.save(gameCreator.createNewGame(match));
                    return new ResponseEntity<>(new StartResponseWrapper(match.getPlayer2Id().toString(),
                            match.getRoomId().toString()), HttpStatus.OK);
                }
            } else
                return new ResponseEntity<>("Cannot find your account! Please reenter the game and try again!", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>("An error has occurred (the board is invalid)! Please try again and send a valid board.", HttpStatus.BAD_REQUEST);

    }



    @GetMapping("/lobby")
    public ResponseEntity lobby(@RequestParam(name = "roomId") String roomId) {
        Match match = matchRepo.findByRoomId(UUID.fromString(roomId));
        if (match == null || match.getPlayer2Name() == null) {
            PrivateMatch privateMatch = privateMatchRepo.findByRoomId(UUID.fromString(roomId));
            if (privateMatch == null || privateMatch.getPlayer2Name() == null){
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity(HttpStatus.OK);
            }
        } else {
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @GetMapping("/scoreboard")
    public ResponseEntity<List<Scoreboard>> scoreboard() {
        List<Scoreboard> response = scoreboardRepo.findTop10ByOrderByWinsDesc();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/room")
    public ResponseEntity createRoom(@RequestParam(name = "roomName") String roomName,
                                       @RequestParam(name = "password") String password,
                                       @RequestParam(name = "playerId") String playerId) {
        if (roomName == null || roomName.equals("null") || password == null || password.equals("null") ||
                playerId == null || playerId.equals("null") || roomName.length() == 0 || password.length() == 0 ||
                playerId.length() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            PrivateMatch matchFromDB = privateMatchRepo.findByRoomNameAndPlayer2IdEquals(roomName, null);
            if (matchFromDB == null) {
                PrivateMatch match = new PrivateMatch();
                match.setRoomName(roomName);
                match.setPassword(password);
                match.setRoomId(UUID.randomUUID());
                match.setDate(new Date());
                match.setPlayer1Id(UUID.fromString(playerId));
                match.setPlayer1Name(userRepo.findByUuid(UUID.fromString(playerId)).getUsername());
                privateMatchRepo.save(match);
                return new ResponseEntity<>(match.getRoomId(), HttpStatus.OK);
            } else return new ResponseEntity<>("The room with this name already exists.", HttpStatus.OK);
        }
    }

    @GetMapping("/roomlist")
    public ResponseEntity<List<Scoreboard>> roomList() {
        List<Scoreboard> response = scoreboardRepo.findTop10ByOrderByWinsDesc();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
