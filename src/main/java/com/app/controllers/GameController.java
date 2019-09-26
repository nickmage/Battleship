package com.app.controllers;

import com.app.entities.Matchmaking;
import com.app.entities.Ship;
import com.app.repo.MatchmakingRepo;
import com.app.validation.BoardValidator;
import com.auth.entities.User;
import com.auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
public class GameController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MatchmakingRepo matchmakingRepo;

    @RequestMapping(value = "/start", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> getShips(@RequestBody Ship[] ships,
                                           @RequestHeader(name = "Username") String username) {
        System.out.println(username);
        if (new BoardValidator(ships).isValidBoard()) {
            User userFromDB = userRepo.findByUsername(username);
            if (userFromDB != null) {
                String id = userFromDB.getUuid().toString();
                return new ResponseEntity<>(id, HttpStatus.OK);
            } else
                return new ResponseEntity<>("Cannot find your account! Please reenter the game and try again!", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>("An error has occurred (the board is invalid)! Please try again and send a valid board.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/start/matchmaking")
    @ResponseBody
    public ResponseEntity matchmaking(@RequestHeader(name = "Username") String username,
                                   @RequestParam(name = "playerId") String id) {
        if (username == null || id == null) {
            return new ResponseEntity<>("An error has occurred! (null)", HttpStatus.BAD_REQUEST);
        }
        UUID playerId = UUID.fromString(id);

        //find free room
        List<Matchmaking> queue = matchmakingRepo.findByPlayer2Name(null);
        //remove rooms to avoid playing with itself
        queue.removeIf(match -> match.getPlayer1Id().equals(playerId));//testing required

        //create new room
        if (queue.size() == 0) {
            Matchmaking newRoom = new Matchmaking();
            newRoom.setPlayer1Name(username);
            newRoom.setPlayer1Id(playerId);
            newRoom.setRoomId(UUID.randomUUID());
            newRoom.setDate(new Date());
            matchmakingRepo.save(newRoom);
            //System.out.println("New room created!");
            return new ResponseEntity<>(newRoom.getRoomId().toString(), HttpStatus.OK);
        } else {
            Matchmaking existingRoom = queue.get(0);
            existingRoom.setPlayer2Name(username);
            existingRoom.setPlayer2Id(playerId);
            matchmakingRepo.save(existingRoom);
            //System.out.println("Player 2 found!");
            return new ResponseEntity<>(existingRoom.getRoomId().toString(), HttpStatus.OK);
        }
    }
}
