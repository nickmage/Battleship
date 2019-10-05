package com.app.services;

import com.app.DTOs.GameDTO;
import com.app.DTOs.ShotDTO;
import com.app.cache.Room;
import com.app.entities.BoardCell;
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

    public Room restore(GameDTO game) throws IOException {
        Room room = new Room();
        room.setCurrentPlayer(game.getCurrentPlayer());

        room.setPlayer1Name(game.getPlayer1Name());
        room.setPlayer1Id(game.getPlayer1Id());

        room.setPlayer2Name(game.getPlayer2Name());
        room.setPlayer2Id(game.getPlayer2Id());

        room.setWinner(game.getWinner());

        setBoards(game, room);

        return room;
    }

    private void setBoards(GameDTO game, Room room) throws IOException {
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

    private ArrayList<BoardCell> getPlayerBoard(ArrayList<ArrayList<BoardCell>> ships, UUID roomId, UUID enemyId, int number){
        ArrayList<BoardCell> playerBoard = new ArrayList<>();
        for (ArrayList<BoardCell> ship : ships) {
            playerBoard.addAll(ship);
        }
        List<ShotDTO> shots = getShotList(roomId, enemyId, number);
        fillBoardWithShots(playerBoard, shots);
        return playerBoard;
    }

    private ArrayList<BoardCell> getEnemyBoard( UUID roomId, UUID enemyId, int number){
        ArrayList<BoardCell> enemyBoard = new ArrayList<>();
        List<ShotDTO> shots = getShotList(roomId, enemyId, number);
        fillBoardWithShots(enemyBoard, shots);
        return enemyBoard;
    }

    private void fillBoardWithShots(ArrayList<BoardCell> board, List<ShotDTO> shots){
        if (shots != null && shots.size() != 0) {
            for (ShotDTO shot:shots) {
                BoardCell shotCell = new BoardCell();
                shotCell.setX(shot.getX());
                shotCell.setY(shot.getY());
                shotCell.setValue(shot.getValue());
                board.add(shotCell);
            }
        }
    }

    private List<ShotDTO> getShotList(UUID roomId, UUID playerId, int number){
        if (number == 1){
            return shotRepo.findByRoomIdAndPlayer1IdAndValueEquals(roomId, playerId, 0);
        } else {
            return shotRepo.findByRoomIdAndPlayer2IdAndValueEquals(roomId, playerId, 0);
        }
    }


}
