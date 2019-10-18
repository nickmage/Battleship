package com.app.services;

import com.app.cache.Room;
import com.app.entities.Game;

import com.app.cache.RoomCache;
import com.app.entities.GameType;
import com.app.models.BoardCell;
import com.app.models.Ship;
import com.app.repo.GameRepo;
import com.app.response_wrappers.StartResponseWrapper;
import com.auth.entities.User;
import com.auth.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameCreator {
    private final GameRepo gameRepo;
    private final ObjectMapper objectMapper;
    private final BoardCreator boardCreator;
    private final UserRepo userRepo;

    public GameCreator(GameRepo gameRepo, ObjectMapper objectMapper, BoardCreator boardCreator, UserRepo userRepo) {
        this.gameRepo = gameRepo;
        this.objectMapper = objectMapper;
        this.boardCreator = boardCreator;
        this.userRepo = userRepo;
    }

    public ResponseEntity getResponseForPublicGame(Ship[] ships, User userFromDB) throws JsonProcessingException {
        String username = userFromDB.getUsername();
        List<Game> freeRooms = gameRepo.findByPlayer2NameIsNullAndPlayer1NameNotAndTypeEquals(username, GameType.PUBLIC);
        //remove rooms to avoid playing with himself
        //freeRooms.removeIf(match -> match.getPlayer1Id().equals(playerId));
        if (freeRooms.size() == 0) {
            Game game = createPublicGame(userFromDB, ships);
            gameRepo.save(game);
            return new ResponseEntity<>(new StartResponseWrapper(game.getPlayer1Id().toString(),
                    game.getRoomId().toString()), HttpStatus.OK);
        } else {
            Game game = joinExistingPublicGame(freeRooms.get(0), userFromDB, ships);
            gameRepo.save(game);
            //gameRepo.save(gameCreator.createNewPublicGame(match));
            return new ResponseEntity<>(new StartResponseWrapper(game.getPlayer2Id().toString(),
                    game.getRoomId().toString()), HttpStatus.OK);
        }
    }

    private Game createPublicGame(User user, Ship[] shipStartCells) throws JsonProcessingException {
        Game game = new Game();
        game.setRoomId(UUID.randomUUID());
        game.setPlayer1Name(user.getUsername());
        game.setPlayer1Id(user.getId());
        ArrayList<ArrayList<BoardCell>> ships = boardCreator.getShips(shipStartCells);
        game.setPlayer1Ships(objectMapper.writeValueAsString(ships));
        game.setDate(new Date());
        game.setCurrentPlayer(new Random().nextInt(2) + 1);
        game.setType(GameType.PUBLIC);
        game.setWinner(0);
        cacheNewPublicRoom(game, ships, shipStartCells);
        return game;
    }

    private void cacheNewPublicRoom(Game game, ArrayList<ArrayList<BoardCell>> ships, Ship[] shipStartCells) {
        Room room = new Room();
        fillNewRoom(game, room);
        room.setPlayer1Board(boardCreator.getBoard(shipStartCells));
        room.setPlayer1Ships(ships);
        RoomCache.rooms.put(game.getRoomId().toString(), room);
    }

    private Game joinExistingPublicGame(Game game, User user, Ship[] shipStartCells) throws JsonProcessingException {
        game.setPlayer2Name(user.getUsername());
        game.setPlayer2Id(user.getId());
        ArrayList<ArrayList<BoardCell>> ships = boardCreator.getShips(shipStartCells);
        game.setPlayer2Ships(objectMapper.writeValueAsString(ships));
        cacheExistingPublicGame(game, ships, shipStartCells);
        return game;
    }

    private void cacheExistingPublicGame(Game game, ArrayList<ArrayList<BoardCell>> ships, Ship[] shipStartCells) {
        Room room = RoomCache.rooms.get(game.getRoomId().toString());
        room.setPlayer2Name(game.getPlayer2Name());
        room.setPlayer2Id(game.getPlayer2Id());
        room.setPlayer2Board(boardCreator.getBoard(shipStartCells));
        room.setPlayer2Ships(ships);
    }









    public ResponseEntity createPrivateGame(String roomName, String password, String username){
        Game game = new Game();
        game.setRoomName(roomName);
        game.setPassword(password);
        game.setRoomId(UUID.randomUUID());
        game.setDate(new Date());
        game.setPlayer1Id(userRepo.findByUsername(username).getId());
        game.setPlayer1Name(username);
        game.setType(GameType.PRIVATE);
        game.setCurrentPlayer(new Random().nextInt(2) + 1);
        game.setWinner(0);
        cachePrivateRoom(game);
        gameRepo.save(game);
        return new ResponseEntity<>(game.getRoomId(), HttpStatus.OK);
    }

    private void cachePrivateRoom(Game game) {
        Room room = new Room();
        fillNewRoom(game, room);
        RoomCache.rooms.put(game.getRoomId().toString(), room);
    }

    private void fillNewRoom(Game game, Room room){
        room.setRoomId(game.getRoomId());
        room.setPlayer1Name(game.getPlayer1Name());
        room.setPlayer1Id(game.getPlayer1Id());
        room.setCurrentPlayer(game.getCurrentPlayer());
    }


    public ResponseEntity getResponseForPrivateGame(Ship[] shipStartCells, User user, String roomId) throws JsonProcessingException {
        Game game = gameRepo.findByRoomIdAndTypeEquals(UUID.fromString(roomId), GameType.PRIVATE);
        ArrayList<ArrayList<BoardCell>> ships = boardCreator.getShips(shipStartCells);
        Room room = RoomCache.rooms.get(roomId);
        if (game.getPlayer1Name().equals(user.getUsername())) {
            game.setPlayer1Ships(objectMapper.writeValueAsString(ships));
            gameRepo.save(game);
            cachePlayer1Data(room, ships, shipStartCells);
            return new ResponseEntity<>(new StartResponseWrapper(game.getPlayer1Id().toString(),
                    game.getRoomId().toString()), HttpStatus.OK);
        } else {
            game.setPlayer2Name(user.getUsername());
            game.setPlayer2Id(user.getId());
            game.setPlayer2Ships(objectMapper.writeValueAsString(ships));
            gameRepo.save(game);
            cachePlayer2Data(game, room, ships, shipStartCells);
            return new ResponseEntity<>(new StartResponseWrapper(game.getPlayer2Id().toString(),
                    game.getRoomId().toString()), HttpStatus.OK);
        }
    }


    private void cachePlayer1Data(Room room, ArrayList<ArrayList<BoardCell>> ships, Ship[] shipStartCells){
        room.setPlayer1Ships(ships);
        room.setPlayer1Board(boardCreator.getBoard(shipStartCells));
    }

    private void cachePlayer2Data(Game game, Room room, ArrayList<ArrayList<BoardCell>> ships, Ship[] shipStartCells){
        room.setPlayer2Name(game.getPlayer2Name());
        room.setPlayer2Id(game.getPlayer2Id());
        room.setPlayer2Ships(ships);
        room.setPlayer2Board(boardCreator.getBoard(shipStartCells));
    }






    /*public Game createNewRoom(User user, Ship[] shipStartCells) throws JsonProcessingException {
        match.setPlayer1Name(user.getUsername());
        match.setPlayer1Id(user.getId());
        match.setRoomId(UUID.randomUUID());
        match.setDate(new Date());
        ArrayList<ArrayList<BoardCell>> ships = boardCreator.getShips(shipStartCells);
        match.setPlayer1Ships(objectMapper.writeValueAsString(ships));
        match.setType(MatchType.PUBLIC);
        cacheNewRoom(ships, shipStartCells, match.getRoomId());
        return game;
    }

    */











}
