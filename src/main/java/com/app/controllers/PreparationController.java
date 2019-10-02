package com.app.controllers;

import com.app.DTOs.Matchmaking;
import com.app.entities.Ship;
import com.app.repo.GameRepo;
import com.app.repo.MatchmakingRepo;
import com.app.response_wrappers.StartResponseWrapper;
import com.app.services.MatchCreator;
import com.app.validation.BoardValidator;
import com.auth.entities.User;
import com.auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class PreparationController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MatchmakingRepo matchmakingRepo;
    @Autowired
    private MatchCreator matchCreator;
    @Autowired
    private GameRepo gameRepo;

    @PostMapping("/start")
    @ResponseBody
    public ResponseEntity getBoard(@RequestBody Ship[] ships,
                                   @RequestHeader(name = "Username") String username) throws IOException {
        if (new BoardValidator(ships).isValidBoard()) {
            User userFromDB = userRepo.findByUsername(username);
            if (userFromDB != null) {
                UUID playerId = userFromDB.getUuid();
                List<Matchmaking> freeRooms = matchmakingRepo.findByPlayer2Name(null);
                //remove rooms to avoid playing with himself
                freeRooms.removeIf(match -> match.getPlayer1Id().equals(playerId));
                if (freeRooms.size() == 0) {
                    Matchmaking match = matchCreator.createNewRoom(userFromDB, ships);
                    matchmakingRepo.save(match);
                    return new ResponseEntity<>(new StartResponseWrapper(match.getPlayer1Id().toString(),
                            match.getRoomId().toString()), HttpStatus.OK);
                } else {
                    Matchmaking match = matchCreator.joinExistingRoom(freeRooms.get(0), userFromDB, ships);
                    matchmakingRepo.save(match);
                    gameRepo.save(matchCreator.createNewGame(match));
                    return new ResponseEntity<>(new StartResponseWrapper(match.getPlayer2Id().toString(),
                            match.getRoomId().toString()),HttpStatus.OK);
                }
            } else return new ResponseEntity<>("Cannot find your account! Please reenter the game and try again!", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("An error has occurred (the board is invalid)! Please try again and send a valid board.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/lobby")
    @ResponseBody
    public String lobby(@RequestParam(name = "roomId") String roomId) {
        Matchmaking match = matchmakingRepo.findByRoomId(UUID.fromString(roomId));
        if (match == null || match.getPlayer2Name() == null) {
            return null;
        } else {
            return "yes";
        }
    }
}