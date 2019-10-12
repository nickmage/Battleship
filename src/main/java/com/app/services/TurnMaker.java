package com.app.services;

import com.app.entities.Game;
import com.app.entities.Shot;
import com.app.cache.Room;
import com.app.models.BoardCell;
import com.app.exception.WinnerException;
import com.app.models.ShipOrientation;
import com.app.repo.GameRepo;
import com.app.repo.ShotRepo;
import com.app.response_wrappers.ShotResponseWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class TurnMaker {

    private final int MISS = 0;
    private final int HIT = -1;
    private final int BOARD_SIZE = 10;
    private final GameRepo gameRepo;
    private final ShotRepo shotRepo;
    private final ObjectMapper objectMapper;
    private final RemainingShipsCreator remainingShips;
    private final ScoreboardSaver scoreboardSaver;

    public TurnMaker(GameRepo gameRepo, ShotRepo shotRepo,
                     ObjectMapper objectMapper, RemainingShipsCreator remainingShips, ScoreboardSaver scoreboardSaver) {
        this.gameRepo = gameRepo;
        this.shotRepo = shotRepo;
        this.objectMapper = objectMapper;
        this.remainingShips = remainingShips;
        this.scoreboardSaver = scoreboardSaver;
    }

    public ShotResponseWrapper makeShot(Room room, int x, int y) throws JsonProcessingException {
        ShotResponseWrapper response = new ShotResponseWrapper();
        int status = checkShotStatus(room, x, y, response);
        response.setMyTurn(status != MISS);
        try {
            response.setRemainingEnemyShips(remainingShips.getRemainingShips(room.getCurrentPlayer() == 1 ?
                                            room.getPlayer2Ships() : room.getPlayer1Ships()));
        } catch (WinnerException e) {
            room.setWinner(1);
            response.setRemainingEnemyShips(remainingShips.getDestroyedShips());
            Game game = gameRepo.findByRoomId(room.getRoomId());
            game.setWinner(game.getCurrentPlayer());
            gameRepo.save(game);
            int winner = room.getCurrentPlayer();
            int thisPlayer = 1;
            response.setWinner(thisPlayer);
            if (winner == 1){
                scoreboardSaver.storeScoreboard(room.getPlayer1Name(), room.getPlayer2Name());
            } else {
                scoreboardSaver.storeScoreboard(room.getPlayer2Name(), room.getPlayer1Name());
            }
            setRoomCurrentPlayer(room, status);
            return response;
        }
        setRoomCurrentPlayer(room, status);
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
            storeGameToDB(room.getRoomId(), room.getPlayer2Ships(), currentPlayer);
            storeShotToDB(room.getRoomId(), room.getPlayer1Id(), x, y, value);
        } else {
            ArrayList<BoardCell> player1Board = room.getPlayer1Board();
            int value = setShipHit(player1Board, x, y);
            room.getEnemyBoardForPlayer2().add(cell);
            storeGameToDB(room.getRoomId(), room.getPlayer1Ships(), currentPlayer);
            storeShotToDB(room.getRoomId(), room.getPlayer2Id(), x, y, value);
        }
    }

    private void saveSunken(Room room, ArrayList<BoardCell> ship, ArrayList<BoardCell> interactedCells) throws JsonProcessingException {
        ArrayList<BoardCell> sunkenShipSurroundings = new ArrayList<>();
        int y = ship.get(0).getY();
        int x = ship.get(0).getX();
        ShipOrientation orientation = shipOrientation(ship);
        if (orientation == ShipOrientation.NONE || orientation == ShipOrientation.VERTICAL) {
            saveSunkenVerticalShip(ship, sunkenShipSurroundings, x, y);
        } else {
            saveSunkenHorizontalShip(ship, sunkenShipSurroundings, x, y);
        }
        if (room.getCurrentPlayer() == 1) {
            ArrayList<BoardCell> player2Board = room.getPlayer2Board();
            player2Board.addAll(sunkenShipSurroundings);
            room.getEnemyBoardForPlayer1().addAll(sunkenShipSurroundings);
            storeGameToDB(room.getRoomId(), room.getPlayer2Ships(), 1);
        } else {
            ArrayList<BoardCell> player1Board = room.getPlayer1Board();
            player1Board.addAll(sunkenShipSurroundings);
            room.getEnemyBoardForPlayer2().addAll(sunkenShipSurroundings);
            storeGameToDB(room.getRoomId(), room.getPlayer1Ships(), 2);
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

    private ShipOrientation shipOrientation(ArrayList<BoardCell> ship) {
        if (ship.size() == 1) {
            return ShipOrientation.NONE;
        } else if (ship.get(0).getX() == ship.get(1).getX()) {
            return ShipOrientation.HORIZONTAL;
        } else {
            return ShipOrientation.VERTICAL;
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
            storeGameToDB(room.getRoomId(), room.getPlayer2Ships(), currentPlayer);
            storeShotToDB(room.getRoomId(), room.getPlayer1Id(), x, y, MISS);
        } else {
            ArrayList<BoardCell> player1Board = room.getPlayer1Board();
            player1Board.add(cell);
            room.getEnemyBoardForPlayer2().add(cell);
            storeGameToDB(room.getRoomId(), room.getPlayer1Ships(), currentPlayer);
            storeShotToDB(room.getRoomId(), room.getPlayer2Id(), x, y, MISS);
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

    private void storeShotToDB(UUID roomId, UUID playerId, int x, int y, int value){
        Shot shot = new Shot();
        shot.setRoomId(roomId);
        shot.setPlayerId(playerId);
        shot.setX(x);
        shot.setY(y);
        shot.setValue(value);
        shotRepo.save(shot);
    }

    private void storeGameToDB(UUID roomId, ArrayList<ArrayList<BoardCell>> ships, int currentPlayer) throws JsonProcessingException {
        Game game = gameRepo.findByRoomId(roomId);
        if (currentPlayer == 1){
            game.setPlayer2Ships(objectMapper.writeValueAsString(ships));
        } else {
            game.setPlayer1Ships(objectMapper.writeValueAsString(ships));
        }
        gameRepo.save(game);
    }

}
