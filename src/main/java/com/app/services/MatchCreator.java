package com.app.services;

import com.app.entities.Game;
import com.app.entities.Matchmaking;
import com.app.entities.RemainingShips;
import com.app.entities.Ship;
import com.app.repo.GameRepo;
import com.app.repo.MatchmakingRepo;
import com.app.response_wrappers.StartResponseWrapper;
import com.app.validation.BoardValidator;
import com.auth.entities.User;
import com.auth.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class MatchCreator {

    public Matchmaking createNewRoom(User user, Ship[] ships) throws JsonProcessingException {
        Matchmaking match = new Matchmaking();
        match.setPlayer1Name(user.getUsername());
        match.setPlayer1Id(user.getUuid());
        match.setRoomId(UUID.randomUUID());
        match.setDate(new Date());
        match.setPlayer1BoardJSON(new ObjectMapper().writeValueAsString(ships));
        return match;
    }

    public Matchmaking joinExistingRoom(Matchmaking match, User user, Ship[] ships) throws JsonProcessingException {        ;
        match.setPlayer2Name(user.getUsername());
        match.setPlayer2Id(user.getUuid());
        match.setPlayer2BoardJSON(new ObjectMapper().writeValueAsString(ships));
        return match;
    }

    public Game createNewGame(Matchmaking match) throws IOException {
        Game newGame = new Game();
        ObjectMapper objectMapper = new ObjectMapper();
        BoardCreator boardCreator = new BoardCreator();
        newGame.setRoomId(match.getRoomId());
        newGame.setCurrentPlayer(new Random().nextInt(2) + 1);
        newGame.setPlayer1Name(match.getPlayer1Name());
        newGame.setPlayer1Id(match.getPlayer1Id());
        newGame.setPlayer2Name(match.getPlayer2Name());
        newGame.setPlayer2Id(match.getPlayer2Id());
        newGame.setDate(new Date());
        newGame.setPlayer1BoardJSON(objectMapper.writeValueAsString(boardCreator.getBoard(
                objectMapper.readValue(match.getPlayer1BoardJSON(), Ship[].class))));

        newGame.setPlayer2BoardJSON(objectMapper.writeValueAsString(boardCreator.getBoard(
                objectMapper.readValue(match.getPlayer2BoardJSON(), Ship[].class))));
        newGame.setShipsOfPlayer1JSON(objectMapper.writeValueAsString(new RemainingShips()));
        newGame.setShipsOfPlayer2JSON(objectMapper.writeValueAsString(new RemainingShips()));

        newGame.setWinner(0);
        return newGame;

  }




}

/*

 */



//            /**CREATE ALL BOARDS IN DB**/


//            Game newGame = new Game();
//            newGame.setRoomId(existingRoom.getRoomId());
//            newGame.setCurrentPlayer(new Random().nextInt(2) + 1);
//            newGame.setPlayer1Name(existingRoom.getPlayer1Name());
//            newGame.setPlayer1Id(existingRoom.getPlayer1Id());
//            newGame.setPlayer2Name(existingRoom.getPlayer2Name());
//            newGame.setPlayer2Id(existingRoom.getPlayer2Id());
//            newGame.setDate(new Date());
//            newGame.setPlayer1BoardJSON(new ObjectMapper().writeValueAsString(boardCreator.getBoard(
//
//                    new ObjectMapper().readValue(existingRoom.getPlayer1BoardJSON(), Ship[].class)
//            )));
//            newGame.setEnemyBoardForPlayer1JSON(null);




//            return new ResponseEntity<>(new StartResponseWrapper(match.getPlayer2Id().toString(),
//                    match.getRoomId().toString()),HttpStatus.OK);
//        }
//    }

/*        if (new BoardValidator(ships).isValidBoard()) {
            User userFromDB = userRepo.findByUsername(username);
            if (userFromDB != null) {
                Matchmaking match = matchCreator.create(userFromDB, ships);
                matchmakingRepo.save(match);

                    //System.out.println("Player 2 found!");


            } else
                return new ResponseEntity<>("Cannot find your account! Please reenter the game and try again!", HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>("An error has occurred (the board is invalid)! Please try again and send a valid board.", HttpStatus.BAD_REQUEST);
    }*/