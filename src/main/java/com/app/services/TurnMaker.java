package com.app.services;

import com.app.DTOs.GameDTO;
import com.app.DTOs.ShotDTO;
import com.app.cache.Room;
import com.app.entities.BoardCell;
import com.app.exception.WinnerException;
import com.app.repo.GameRepo;
import com.app.repo.ShotRepo;
import com.app.response_wrappers.ShotResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TurnMaker {

    private final int MISS = 0;
    private final int HIT = -1;
    private final int SINK = 1;
    private final int PLAYER = 1;
    private final int OPPONENT = -1;
    private final int BOARD_SIZE = 10;
    private final char HORIZONTAL = 'h';
    private final char VERTICAL = 'v';
    private final char NONE = '-';
    private final GameRepo gameRepo;
    private final ShotRepo shotRepo;
    private final ObjectMapper objectMapper;
    private final RemainingShipsCreator remainingShips;

    public TurnMaker(GameRepo gameRepo, ShotRepo shotRepo,
                     ObjectMapper objectMapper, RemainingShipsCreator remainingShips) {
        this.gameRepo = gameRepo;
        this.shotRepo = shotRepo;
        this.objectMapper = objectMapper;
        this.remainingShips = remainingShips;
    }

    /*
    P1
    Room ->
    * check player2Ships if hit/miss
     change player2Ships respectively
     add new cells / change existing to player2Board(if miss/hit resp.)
     add new cells to enemyBoardForPlayer1

    Response ->
    send interactedCells
    calculate remainingEnemyShips
    find out if myTurn;
    set winner if it is present

    DB ->
    add new record to Shots
    modify player2 ships in Game table

    */

    public ShotResponseWrapper makeShot(Room room, int x, int y) throws JsonProcessingException {
        ShotResponseWrapper response = new ShotResponseWrapper();
        int status = checkShotStatus(room, x, y, response);
        response.setMyTurn(status != MISS);
        setRoomCurrentPlayer(room, status);
        try {
            response.setRemainingEnemyShips(remainingShips.getRemainingShips(room.getCurrentPlayer() == 1 ?
                    room.getPlayer2Ships() : room.getPlayer1Ships()));
        } catch (WinnerException e) {
            response.setWinner(room.getCurrentPlayer() == 1 ? PLAYER : OPPONENT);
            return response;
        }
        response.setWinner(0);
        return response;
    }

    private int checkShotStatus(Room room, int x, int y, ShotResponseWrapper response) throws JsonProcessingException {
        ArrayList<BoardCell> interactedCells = new ArrayList<>();
        int currentPlayer = room.getCurrentPlayer();
        ArrayList<ArrayList<BoardCell>> enemyShips = (currentPlayer == 1) ? room.getPlayer2Ships() : room.getPlayer1Ships();
        for (ArrayList<BoardCell> ship : enemyShips) {
            if (isShipHit(ship, x, y)) {
                if (isShipSunken(ship)) {
                    saveSunken(room, ship, interactedCells);
                    fillListsWithCells(room, x, y, response, interactedCells, currentPlayer, true);
                    return SINK;
                }
                fillListsWithCells(room, x, y, response, interactedCells, currentPlayer, true);
                return HIT;
            }
        }
        fillListsWithCells(room, x, y, response, interactedCells, currentPlayer, false);
        return MISS;
    }

    private void fillListsWithCells(Room room, int x, int y, ShotResponseWrapper response, ArrayList<BoardCell> interactedCells,
                                    int currentPlayer, boolean isHit) throws JsonProcessingException {
        if (isHit) {
            saveHit(room, x, y, currentPlayer);
            interactedCells.add(new BoardCell(x, y, HIT));
        } else {
            saveMiss(room, x, y, currentPlayer);
            interactedCells.add(new BoardCell(x, y, MISS));
        }
        response.setInteractedCells(interactedCells);
    }

    private boolean isShipHit(ArrayList<BoardCell> ship, int x, int y) {
        for (BoardCell cell : ship) {
            if (cell.getX() == x && cell.getY() == y) {
                cell.setValue(-cell.getValue());
                return true;
            }
        }
        return false;
    }

    private void saveHit(Room room, int x, int y, int currentPlayer) throws JsonProcessingException {
        BoardCell cell = new BoardCell(x, y, HIT);
        if (currentPlayer == 1) {
            ArrayList<BoardCell> player2Board = room.getPlayer2Board();
            int value = setShipHit(player2Board, x, y);
            room.getEnemyBoardForPlayer1().add(cell);
            GameDTO game = gameRepo.findByRoomId(room.getRoomId());
            game.setPlayer2Ships(objectMapper.writeValueAsString(room.getPlayer2Ships()));
            gameRepo.save(game);
            ShotDTO shot = new ShotDTO();
            shot.setRoomId(room.getRoomId());
            shot.setPlayerId(room.getPlayer1Id());
            shot.setX(x);
            shot.setY(y);
            shot.setValue(value);
            shotRepo.save(shot);
        } else {
            ArrayList<BoardCell> player1Board = room.getPlayer1Board();
            int value = setShipHit(player1Board, x, y);
            room.getEnemyBoardForPlayer2().add(cell);
            GameDTO game = gameRepo.findByRoomId(room.getRoomId());
            game.setPlayer2Ships(objectMapper.writeValueAsString(room.getPlayer2Ships()));
            gameRepo.save(game);
            ShotDTO shot = new ShotDTO();
            shot.setRoomId(room.getRoomId());
            shot.setPlayerId(room.getPlayer2Id());
            shot.setX(x);
            shot.setY(y);
            shot.setValue(value);
            shotRepo.save(shot);
        }
    }

    private void saveSunken(Room room, ArrayList<BoardCell> ship, ArrayList<BoardCell> interactedCells) throws JsonProcessingException {
        ArrayList<BoardCell> sunkenShipSurroundings = new ArrayList<>();
        char orientation = shipOrientation(ship);
        int y = ship.get(0).getY();
        int x = ship.get(0).getX();
        if (orientation == NONE || orientation == VERTICAL) {
            saveSunkenVerticalShip(ship, sunkenShipSurroundings, x, y);
        } else {
            saveSunkenHorizontalShip(ship, sunkenShipSurroundings, x, y);
        }
        if (room.getCurrentPlayer() == 1) {
            ArrayList<BoardCell> player2Board = room.getPlayer2Board();
            player2Board.addAll(sunkenShipSurroundings);
            room.getEnemyBoardForPlayer1().addAll(sunkenShipSurroundings);
            GameDTO game = gameRepo.findByRoomId(room.getRoomId());
            game.setPlayer2Ships(objectMapper.writeValueAsString(room.getPlayer2Ships()));
            gameRepo.save(game);
            /*for (BoardCell cell : sunkenShipSurroundings) {
                ShotDTO shot = new ShotDTO();
                shot.setRoomId(room.getRoomId());
                shot.setPlayerId(room.getPlayer1Id());
                shot.setX(cell.getX());
                shot.setY(cell.getX());
                shot.setValue(MISS);
                shotRepo.save(shot);
            }*/
        } else {
            ArrayList<BoardCell> player1Board = room.getPlayer1Board();
            player1Board.addAll(sunkenShipSurroundings);
            room.getEnemyBoardForPlayer2().addAll(sunkenShipSurroundings);
            GameDTO game = gameRepo.findByRoomId(room.getRoomId());
            game.setPlayer1Ships(objectMapper.writeValueAsString(room.getPlayer1Ships()));
            gameRepo.save(game);
            /*for (BoardCell cell : sunkenShipSurroundings) {
                ShotDTO shot = new ShotDTO();
                shot.setRoomId(room.getRoomId());
                shot.setPlayerId(room.getPlayer1Id());
                shot.setX(cell.getX());
                shot.setY(cell.getX());
                shot.setValue(MISS);
                shotRepo.save(shot);
            }*/
        }
        interactedCells.addAll(sunkenShipSurroundings);
    }

    private void saveSunkenHorizontalShip(ArrayList<BoardCell> ship, ArrayList<BoardCell> sunkenShipSurroundings, int x, int y){
        if (x - 1 >= 0) {
            for (BoardCell cell : ship) {
                int currentY = cell.getY();
                sunkenShipSurroundings.add(new BoardCell(x - 1, currentY, MISS));
            }
        }
        if (x + 1 < BOARD_SIZE) {
            for (BoardCell cell : ship) {
                int currentY = cell.getY();
                sunkenShipSurroundings.add(new BoardCell(x + 1, currentY, MISS));
            }
        }
        if (y - 1 >= 0) {
            sunkenShipSurroundings.add(new BoardCell(x, y - 1, MISS));
            if (x - 1 >= 0) {
                sunkenShipSurroundings.add(new BoardCell(x - 1, y - 1, MISS));
            }
            if (x + 1 < BOARD_SIZE) {
                sunkenShipSurroundings.add(new BoardCell(x + 1, y - 1, MISS));
            }
        }
        y = y + Math.abs(ship.get(0).getValue());
        if (y < BOARD_SIZE) {
            sunkenShipSurroundings.add(new BoardCell(x, y, MISS));
            if (x - 1 >= 0) {
                sunkenShipSurroundings.add(new BoardCell(x - 1, y, MISS));
            }
            if (x + 1 < BOARD_SIZE) {
                sunkenShipSurroundings.add(new BoardCell(x + 1, y, MISS));
            }
        }
    }

    private void saveSunkenVerticalShip(ArrayList<BoardCell> ship, ArrayList<BoardCell> sunkenShipSurroundings, int x, int y){
        if (y - 1 >= 0) {
            for (BoardCell cell : ship) {
                int currentX = cell.getX();
                sunkenShipSurroundings.add(new BoardCell(currentX, y - 1, MISS));
            }
        }
        if (y + 1 < BOARD_SIZE) {
            for (BoardCell cell : ship) {
                int currentX = cell.getX();
                sunkenShipSurroundings.add(new BoardCell(currentX, y + 1, MISS));
            }
        }
        if (x - 1 >= 0) {
            sunkenShipSurroundings.add(new BoardCell(x - 1, y, MISS));
            if (y - 1 >= 0) {
                sunkenShipSurroundings.add(new BoardCell(x - 1, y - 1, MISS));
            }
            if (y + 1 < BOARD_SIZE) {
                sunkenShipSurroundings.add(new BoardCell(x - 1, y + 1, MISS));
            }
        }
        x = x + Math.abs(ship.get(0).getValue());
        if (x < BOARD_SIZE) {
            sunkenShipSurroundings.add(new BoardCell(x, y, MISS));
            if (y - 1 >= 0) {
                sunkenShipSurroundings.add(new BoardCell(x, y - 1, MISS));
            }
            if (y + 1 < BOARD_SIZE) {
                sunkenShipSurroundings.add(new BoardCell(x, y + 1, MISS));
            }
        }
    }

    private char shipOrientation(ArrayList<BoardCell> ship) {
        if (ship.size() == 1) {
            return NONE;
        } else if (ship.get(0).getX() == ship.get(1).getX()) {
            return HORIZONTAL;
        } else {
            return VERTICAL;
        }
    }

    private int setShipHit(ArrayList<BoardCell> ship, int x, int y) {
        for (BoardCell cell : ship) {
            if (cell.getX() == x && cell.getY() == y) {
                cell.setValue(-cell.getValue());
                return cell.getValue();
            }
        }
        return 0;
    }

    private boolean isShipSunken(ArrayList<BoardCell> ship) {
        int count = 0;
        for (BoardCell boardCell : ship) {
            if (boardCell.getValue() < 0) {
                count++;
            }
        }
        return count == ship.size();
    }

    private void saveMiss(Room room, int x, int y, int currentPlayer) throws JsonProcessingException {
        BoardCell cell = new BoardCell(x, y, MISS);
        if (currentPlayer == 1) {
            ArrayList<BoardCell> player2Board = room.getPlayer2Board();
            player2Board.add(cell);
            room.getEnemyBoardForPlayer1().add(cell);
            GameDTO game = gameRepo.findByRoomId(room.getRoomId());
            game.setPlayer2Ships(objectMapper.writeValueAsString(room.getPlayer2Ships()));
            gameRepo.save(game);
            ShotDTO shot = new ShotDTO();
            shot.setRoomId(room.getRoomId());
            shot.setPlayerId(room.getPlayer1Id());
            shot.setX(x);
            shot.setY(y);
            shot.setValue(MISS);
            shotRepo.save(shot);
        } else {
            ArrayList<BoardCell> player1Board = room.getPlayer1Board();
            player1Board.add(cell);
            room.getEnemyBoardForPlayer2().add(cell);
            GameDTO game = gameRepo.findByRoomId(room.getRoomId());
            game.setPlayer1Ships(objectMapper.writeValueAsString(room.getPlayer1Ships()));
            gameRepo.save(game);
            ShotDTO shot = new ShotDTO();
            shot.setRoomId(room.getRoomId());
            shot.setPlayerId(room.getPlayer2Id());
            shot.setX(x);
            shot.setY(y);
            shot.setValue(MISS);
            shotRepo.save(shot);
        }

    }

    private void setRoomCurrentPlayer(Room room, int status) {
        int currentPlayer = room.getCurrentPlayer();
        if (currentPlayer == 1 && status == MISS) {
            room.setCurrentPlayer(2);
        } else if (status == MISS) {
            room.setCurrentPlayer(1);
        }
    }

}
