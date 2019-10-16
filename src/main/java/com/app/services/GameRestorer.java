package com.app.services;

import com.app.entities.Game;
import com.app.entities.Shot;
import com.app.cache.Room;
import com.app.models.BoardCell;
import com.app.repo.ShotRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class GameRestorer {
    private final ObjectMapper objectMapper;
    private final ShotRepo shotRepo;
    private final TurnMaker turnMaker;

    public GameRestorer(ObjectMapper objectMapper, ShotRepo shotRepo, TurnMaker turnMaker) {
        this.objectMapper = objectMapper;
        this.shotRepo = shotRepo;
        this.turnMaker = turnMaker;
    }

    Room restore(Game game) throws IOException {
        Room room = new Room();
        room.setRoomId(game.getRoomId());

        room.setCurrentPlayer(game.getCurrentPlayer());

        room.setPlayer1Name(game.getPlayer1Name());
        room.setPlayer1Id(game.getPlayer1Id());

        room.setPlayer2Name(game.getPlayer2Name());
        room.setPlayer2Id(game.getPlayer2Id());

        room.setWinner(game.getWinner());

        setBoards(game, room);

        return room;
    }

    private void setBoards(Game game, Room room) throws IOException {
        room.setPlayer1Ships(mapShipsFromDB(game.getPlayer1Ships()));
        room.setPlayer2Ships(mapShipsFromDB(game.getPlayer2Ships()));

        room.setPlayer1Board(getPlayerBoard(room.getPlayer1Ships(), room.getRoomId(), room.getPlayer2Id()));
        room.setPlayer2Board(getPlayerBoard(room.getPlayer2Ships(), room.getRoomId(), room.getPlayer1Id()));

        room.setEnemyBoardForPlayer1(getEnemyBoard(game.getRoomId(), game.getPlayer1Id(), room.getPlayer2Ships()));
        room.setEnemyBoardForPlayer2(getEnemyBoard(game.getRoomId(), game.getPlayer2Id(), room.getPlayer1Ships()));
     }

    private ArrayList<ArrayList<BoardCell>> mapShipsFromDB(String shipsFromDB) throws IOException {
        BoardCell[][] cells = objectMapper.readValue(shipsFromDB, BoardCell[][].class);
        ArrayList<ArrayList<BoardCell>> ships = new ArrayList<>();
        for (BoardCell[] cell : cells) {
            ArrayList<BoardCell> ship = new ArrayList<>(Arrays.asList(cell));
            ships.add(ship);
        }
        return ships;
    }

    private ArrayList<BoardCell> getPlayerBoard(ArrayList<ArrayList<BoardCell>> ships, UUID roomId, UUID opponentId) {
        ArrayList<BoardCell> playerBoard = new ArrayList<>();
        fillBoardWithShipSurroundings(ships, playerBoard, true);
        List<Shot> shots = getShotListOfMisses(roomId, opponentId);
        for (Shot shot: shots) {
            playerBoard.add(new BoardCell(shot.getX(), shot.getY(), shot.getValue()));
        }
        for (ArrayList<BoardCell> ship : ships) {
            playerBoard.addAll(ship);
        }
        return playerBoard;
    }

    private void fillBoardWithShipSurroundings(ArrayList<ArrayList<BoardCell>> ships, ArrayList<BoardCell> board, boolean isPlayerBoard){
        for (ArrayList<BoardCell> ship : ships) {
            if (turnMaker.isShipSunken(ship)){
                board.addAll(turnMaker.getSunkenShipSurroundings(ship));
            }
            if (isPlayerBoard){
                board.addAll(ship);
            }
        }
    }

    private List<Shot> getShotListOfMisses(UUID roomId, UUID playerId) {
        int miss = 0;
        return shotRepo.findByRoomIdAndPlayerIdAndValueEquals(roomId, playerId, miss);
    }

    private ArrayList<BoardCell> getEnemyBoard(UUID roomId, UUID playerId, ArrayList<ArrayList<BoardCell>> ships) {
        ArrayList<BoardCell> enemyBoard = new ArrayList<>();
        List<Shot> shots = getAllShotList(roomId, playerId);
        fillBoardWithShots(enemyBoard, shots, ships);
        return enemyBoard;
    }

    private List<Shot> getAllShotList(UUID roomId, UUID playerId) {
        return shotRepo.findByRoomIdAndPlayerId(roomId, playerId);
    }

    private void fillBoardWithShots(ArrayList<BoardCell> board, List<Shot> shots, ArrayList<ArrayList<BoardCell>> ships) {
        if (shots != null && shots.size() != 0) {
            for (Shot shot : shots) {
                board.add(new BoardCell(shot.getX(), shot.getY(), shot.getValue() == 0 ? 0 : -1));
            }
        }
        fillBoardWithShipSurroundings(ships, board, false);
    }

}
