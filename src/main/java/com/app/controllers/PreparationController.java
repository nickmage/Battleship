package com.app.controllers;

import com.app.DTOs.MatchDTO;
import com.app.entities.Ship;
import com.app.repo.GameRepo;
import com.app.repo.MatchRepo;
import com.app.response_wrappers.StartResponseWrapper;
import com.app.services.GameCreator;
import com.app.services.MatchCreator;
import com.app.validation.BoardValidator;
import com.auth.entities.User;
import com.auth.repo.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class PreparationController {

    private final UserRepo userRepo;
    private final MatchRepo matchRepo;
    private final MatchCreator matchCreator;
    private final GameRepo gameRepo;
    private final GameCreator gameCreator;

    public PreparationController(UserRepo userRepo, MatchRepo matchRepo, MatchCreator matchCreator, GameRepo gameRepo, GameCreator gameCreator) {
        this.userRepo = userRepo;
        this.matchRepo = matchRepo;
        this.matchCreator = matchCreator;
        this.gameRepo = gameRepo;
        this.gameCreator = gameCreator;
    }

    @PostMapping("/start")
    public ResponseEntity getBoard(@RequestBody Ship[] ships,
                                   @RequestHeader(name = "Username") String username) throws IOException {
        if (new BoardValidator(ships).isValidBoard()) {
            User userFromDB = userRepo.findByUsername(username);
            if (userFromDB != null) {
                UUID playerId = userFromDB.getUuid();
                List<MatchDTO> freeRooms = matchRepo.findByPlayer2Name(null);
                //remove rooms to avoid playing with himself
                freeRooms.removeIf(matchDTO -> matchDTO.getPlayer1Id().equals(playerId));
                if (freeRooms.size() == 0) {
                    MatchDTO matchDTO = matchCreator.createNewRoom(userFromDB, ships);
                    matchRepo.save(matchDTO);
                    return new ResponseEntity<>(new StartResponseWrapper(matchDTO.getPlayer1Id().toString(),
                            matchDTO.getRoomId().toString()), HttpStatus.OK);
                } else {
                    MatchDTO matchDTO = matchCreator.joinExistingRoom(freeRooms.get(0), userFromDB, ships);
                    matchRepo.save(matchDTO);
                    gameRepo.save(gameCreator.createNewGame(matchDTO));
                    return new ResponseEntity<>(new StartResponseWrapper(matchDTO.getPlayer2Id().toString(),
                            matchDTO.getRoomId().toString()),HttpStatus.OK);
                }
            } else return new ResponseEntity<>("Cannot find your account! Please reenter the game and try again!", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("An error has occurred (the board is invalid)! Please try again and send a valid board.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/lobby")
    public ResponseEntity lobby(@RequestParam(name = "roomId") String roomId) {
        MatchDTO matchDTO = matchRepo.findByRoomId(UUID.fromString(roomId));
        if (matchDTO == null || matchDTO.getPlayer2Name() == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(HttpStatus.OK);
        }
    }
}
