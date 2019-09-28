package com.app.services;

import com.app.entities.Game;
import com.app.entities.Matchmaking;
import com.app.entities.RemainingShips;
import com.app.entities.Ship;
import com.auth.entities.User;
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
    @Autowired
    private Matchmaking match;
    @Autowired
    private Game game;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BoardCreator boardCreator;

    public Matchmaking createNewRoom(User user, Ship[] ships) throws JsonProcessingException {
        //Matchmaking match = new Matchmaking();
        match.setPlayer1Name(user.getUsername());
        match.setPlayer1Id(user.getUuid());
        match.setRoomId(UUID.randomUUID());
        match.setDate(new Date());
        match.setPlayer1BoardJSON(objectMapper.writeValueAsString(boardCreator.getBoard(ships)));
        return match;
    }

    public Matchmaking joinExistingRoom(Matchmaking match, User user, Ship[] ships) throws JsonProcessingException {
        match.setPlayer2Name(user.getUsername());
        match.setPlayer2Id(user.getUuid());
        match.setPlayer2BoardJSON(objectMapper.writeValueAsString(boardCreator.getBoard(ships)));
        return match;
    }

    public Game createNewGame(Matchmaking match) throws IOException {
        game.setRoomId(match.getRoomId());
        game.setCurrentPlayer(new Random().nextInt(2) + 1);
        game.setPlayer1Name(match.getPlayer1Name());
        game.setPlayer1Id(match.getPlayer1Id());
        game.setPlayer2Name(match.getPlayer2Name());
        game.setPlayer2Id(match.getPlayer2Id());
        game.setDate(new Date());
        game.setPlayer1BoardJSON(match.getPlayer1BoardJSON());
        game.setPlayer2BoardJSON(match.getPlayer2BoardJSON());
        game.setShipsOfPlayer1JSON(objectMapper.writeValueAsString(new RemainingShips()));
        game.setShipsOfPlayer2JSON(objectMapper.writeValueAsString(new RemainingShips()));
        game.setWinner(0);
        return game;
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