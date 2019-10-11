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

    public GameRestorer(ObjectMapper objectMapper, ShotRepo shotRepo) {
        this.objectMapper = objectMapper;
        this.shotRepo = shotRepo;
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
        room.setPlayer1Ships(getShips(game.getPlayer1Ships()));
        room.setPlayer2Ships(getShips(game.getPlayer2Ships()));

        room.setPlayer1Board(getPlayerBoard(room.getPlayer1Ships(), game.getRoomId(), game.getPlayer2Id(), 2));
        room.setPlayer2Board(getPlayerBoard(room.getPlayer2Ships(), game.getRoomId(), game.getPlayer1Id(), 1));

        room.setEnemyBoardForPlayer1(getEnemyBoard(game.getRoomId(), game.getPlayer1Id(), 1));
        room.setEnemyBoardForPlayer2(getEnemyBoard(game.getRoomId(), game.getPlayer2Id(), 2));
    }

    private ArrayList<ArrayList<BoardCell>> getShips(String shipsFromDB) throws IOException {
        BoardCell[][] cells = objectMapper.readValue(shipsFromDB, BoardCell[][].class);
        ArrayList<ArrayList<BoardCell>> ships = new ArrayList<>();
        for (BoardCell[] cell : cells) {
            ArrayList<BoardCell> ship = new ArrayList<>(Arrays.asList(cell));
            ships.add(ship);
        }
        return ships;
    }

    private ArrayList<BoardCell> getPlayerBoard(ArrayList<ArrayList<BoardCell>> ships, UUID roomId, UUID enemyId, int number) {
        ArrayList<BoardCell> playerBoard = new ArrayList<>();
        for (ArrayList<BoardCell> ship : ships) {
            playerBoard.addAll(ship);
        }
        List<Shot> shots = getShotList(roomId, enemyId, number);
        fillBoardWithShots(playerBoard, shots);
        return playerBoard;
    }

    private ArrayList<BoardCell> getEnemyBoard(UUID roomId, UUID enemyId, int number) {
        ArrayList<BoardCell> enemyBoard = new ArrayList<>();
        List<Shot> shots = getShotList(roomId, enemyId, number);
        fillBoardWithShots(enemyBoard, shots);
        return enemyBoard;
    }

    private void fillBoardWithShots(ArrayList<BoardCell> board, List<Shot> shots) {
        if (shots != null && shots.size() != 0) {
            for (Shot shot : shots) {
                BoardCell shotCell = new BoardCell();
                shotCell.setX(shot.getX());
                shotCell.setY(shot.getY());
                shotCell.setValue(shot.getValue());
                board.add(shotCell);
            }
        }
    }

    private List<Shot> getShotList(UUID roomId, UUID playerId, int number) {
        return shotRepo.findByRoomIdAndPlayerIdAndValueEquals(roomId, playerId, 0);
    }

}
