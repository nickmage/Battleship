package com.app.controllers;

import com.app.entities.Match;
import com.app.entities.PrivateMatch;
import com.app.entities.Scoreboard;
import com.app.models.Ship;
import com.app.repo.GameRepo;
import com.app.repo.MatchRepo;
import com.app.repo.PrivateMatchRepo;
import com.app.repo.ScoreboardRepo;
import com.app.response_wrappers.RoomListResponseWrapper;
import com.app.response_wrappers.ScoreboardWrapper;
import com.app.services.GameCreator;
import com.app.services.MatchCreator;
import com.app.services.MatchSaver;
import com.app.services.PrivateMatchCreator;
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
    private final MatchRepo matchRepo;
    private final ScoreboardRepo scoreboardRepo;
    private final PrivateMatchRepo privateMatchRepo;
    private final MatchSaver matchSaver;
    private final PrivateMatchCreator privateMatchCreator;

    public PreparationController(UserRepo userRepo, MatchRepo matchRepo, ScoreboardRepo scoreboardRepo,
                                 PrivateMatchRepo privateMatchRepo, MatchSaver matchSaver, PrivateMatchCreator privateMatchCreator) {
        this.userRepo = userRepo;
        this.matchRepo = matchRepo;

        this.scoreboardRepo = scoreboardRepo;
        this.privateMatchRepo = privateMatchRepo;
        this.matchSaver = matchSaver;
        this.privateMatchCreator = privateMatchCreator;
    }

    @PostMapping("/start")
    public ResponseEntity getBoard(@RequestBody Ship[] ships,
                                   @RequestHeader(name = "Username") String username,
                                   @RequestHeader(name = "RoomId", required = false) String roomId) throws IOException {
        User userFromDB = userRepo.findByUsername(username);
        if (userFromDB != null) {
            if (new BoardValidator(ships).isValidBoard()) {
                if (roomId == null) {
                    return matchSaver.getResponse(ships, userFromDB);
                } else {
                    return matchSaver.getResponse(ships, userFromDB, roomId);
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
        Match match = matchRepo.findByRoomId(UUID.fromString(roomId));
        if (match == null || match.getPlayer2Name() == null) {
            PrivateMatch privateMatch = privateMatchRepo.findByRoomId(UUID.fromString(roomId));
            if (privateMatch == null || privateMatch.getPlayer2Id() == null || privateMatch.getPlayer1Ships() == null) {
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
            PrivateMatch matchFromDB = privateMatchRepo.findByRoomNameAndPlayer2IdIsNull(roomName);
            if (matchFromDB == null) {
                return privateMatchCreator.createMatch(roomName, password, username);
            } else return new ResponseEntity<>("The room with this name already exists.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/roomlist")
    public ResponseEntity<List<RoomListResponseWrapper>> roomList(@RequestParam(name = "playerName") String playerName) {
        List<PrivateMatch> roomsFromDB = privateMatchRepo.findAllByPlayer2IdIsNullAndPlayer2NameIsNullAndPlayer1NameNot(playerName);
        List<RoomListResponseWrapper> response = new ArrayList<>();
        for (PrivateMatch match : roomsFromDB) {
            response.add(new RoomListResponseWrapper(match.getRoomId(), match.getRoomName(), match.getPlayer1Name()));
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
            return new ResponseEntity<>("Incorrect data.", HttpStatus.BAD_REQUEST);
        } else {
            PrivateMatch match = privateMatchRepo.findByRoomId(UUID.fromString(roomId));
            if (match == null) {
                return new ResponseEntity<>("Room not found.", HttpStatus.BAD_REQUEST);
            } else if (match.getPlayer2Name() != null) {
                return new ResponseEntity<>("This room has been already occupied", HttpStatus.BAD_REQUEST);
            } else if (!match.getPassword().equals(password)) {
                return new ResponseEntity<>("Password is incorrect.", HttpStatus.BAD_REQUEST);
            } else {
                match.setPlayer2Name(username);
                privateMatchRepo.save(match);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }

}
