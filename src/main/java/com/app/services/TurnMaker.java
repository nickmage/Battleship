package com.app.services;

import com.app.DTOs.GameDTO;
import com.app.DTOs.ShotDTO;
import com.app.cache.Room;
import com.app.entities.BoardCell;
import com.app.entities.RemainingShips;
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
    private final int BOARD_SIZE = 10;
    private final char HORIZONTAL = 'h';
    private final char VERTICAL = 'v';
    private final char NONE = '-';
    private final GameRepo gameRepo;
    private final ShotRepo shotRepo;
    private final ObjectMapper objectMapper;

    public TurnMaker(GameRepo gameRepo, ShotRepo shotRepo, ObjectMapper objectMapper) {
        this.gameRepo = gameRepo;
        this.shotRepo = shotRepo;
        this.objectMapper = objectMapper;
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
        response.setRemainingEnemyShips(getRemainingShips(room.getCurrentPlayer() == 1 ? room.getPlayer2Ships() : room.getPlayer1Ships()));
        response.setMyTurn(status != MISS);
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
                    saveHit(room, x, y, currentPlayer);
                    interactedCells.add(new BoardCell(x, y, HIT));
                    saveSunken(room, ship, interactedCells);
                    response.setInteractedCells(interactedCells);
                    return SINK;
                }
                saveHit(room, x, y, currentPlayer);
                interactedCells.add(new BoardCell(x, y, HIT));
                response.setInteractedCells(interactedCells);
                return HIT;
            }
        }
        saveMiss(room, x, y, currentPlayer);
        interactedCells.add(new BoardCell(x, y, MISS));
        response.setInteractedCells(interactedCells);
        return MISS;
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
        //BoardCell cell = new BoardCell(x, y, HIT);
        ArrayList<BoardCell> sunkenShipSurroundings = new ArrayList<>();
        char orientation = shipOrientation(ship);
        int y = ship.get(0).getY();
        int x = ship.get(0).getX();
        if (orientation == NONE || orientation == VERTICAL) {
            if (y - 1 >= 0) {
                for (int i = 0; i < ship.size(); i++) {
                    int currentX = ship.get(i).getX();
                    sunkenShipSurroundings.add(new BoardCell(currentX, y - 1, MISS));
                }
            }
            if (y + 1 < BOARD_SIZE) {
                for (int i = 0; i < ship.size(); i++) {
                    int currentX = ship.get(i).getX();
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
        } else {
            if (x - 1 >= 0) {
                for (int i = 0; i < ship.size(); i++) {
                    int currentY = ship.get(i).getY();
                    sunkenShipSurroundings.add(new BoardCell(x - 1, currentY, MISS));
                }
            }
            if (x + 1 < BOARD_SIZE) {
                for (int i = 0; i < ship.size(); i++) {
                    int currentY = ship.get(i).getY();
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

    private RemainingShips getRemainingShips(ArrayList<ArrayList<BoardCell>> ships) {
        RemainingShips remainingShips = new RemainingShips();
        int oneDeck = 0;
        int twoDeck = 0;
        int threeDeck = 0;
        int fourDeck = 0;
        for (ArrayList<BoardCell> ship : ships) {
            int status = isShipInOrder(ship);
            if (status == 1) {
                oneDeck++;
            } else if (status == 2) {
                twoDeck++;
            } else if (status == 3) {
                threeDeck++;
            } else if (status == 4) {
                fourDeck++;
            }
        }
        remainingShips.setOneDeckShips(oneDeck);
        remainingShips.setTwoDeckShips(twoDeck);
        remainingShips.setThreeDeckShips(threeDeck);
        remainingShips.setFourDeckShips(fourDeck);
        return remainingShips;
    }

    //return 0 if ship is sunken or ship's deck type
    private int isShipInOrder(ArrayList<BoardCell> ship) {
        int deckType = Math.abs(ship.get(0).getValue());
        int count = 0;
        for (BoardCell cell : ship) {
            if (cell.getValue() > 0) {
                count++;
            }
        }
        return count == 0 ? 0 : deckType;
    }




    /*private ArrayList<BoardCell> calculateInteractedCells(Room room, int x, int y) {
        ArrayList<BoardCell> interactedCells = new ArrayList<>();
        ArrayList<ArrayList<BoardCell>> enemyShips;
        if (room.getCurrentPlayer() == 1) {
            enemyShips = room.getPlayer2Ships();
        } else {
            enemyShips = room.getPlayer1Ships();
        }
        int status = isShipHit(enemyShips, x, y, room);

        if (status != MISS) {
            System.out.println("Hit");
            METHODER
            BoardCell cell = new BoardCell();
            cell.setX(x);
            cell.setY(y);
            cell.setValue(status);
            interactedCells.add(cell);
            setShipHit(game, x, y);
            setEnemyBoard(game, x, y, status);
        } else {
            System.out.println("Miss");
           METHODER
            BoardCell cell = new BoardCell();
            cell.setX(x);
            cell.setY(y);
            cell.setValue(MISS);
            interactedCells.add(cell);
            setShipMiss(game, x, y);
            setEnemyBoard(game, x, y, 0);
            room.setCurrentPlayer(room.getCurrentPlayer() == 1 ? 2 : 1);
        }
        return interactedCells;
    }

    private void storeShipsToDatabase() {

    }

    private void storeShotToDatabase(ArrayList<BoardCell> interactedCells) {

    }*/

   /* private void execute(Game game, int x, int y) throws IOException {
        BoardCell[][] enemyShips;
        if (game.getCurrentPlayer() == 1) {
            enemyShips = objectMapper.readValue(game.getShipsOfPlayer2JSON(), BoardCell[][].class);
        } else {
            enemyShips = objectMapper.readValue(game.getShipsOfPlayer1JSON(), BoardCell[][].class);
        }
        int status = isShipHit(enemyShips, x, y);
        if (status < 0) {
            System.out.println("Hit");
            setShipHit(game, x, y);
            setEnemyBoard(game, x, y, status);
        } else {
            System.out.println("Miss");
            setShipMiss(game, x, y);
            setEnemyBoard(game, x, y, 0);
        }
    }*/

    /*@SuppressWarnings("unchecked")
    private void setShipMiss(GameDTO gameDTO, int x, int y) throws IOException {
        List<BoardCell> boardList;
        BoardCell cell = new BoardCell();
        cell.setX(x);
        cell.setY(y);
        cell.setValue(0);
        if (gameDTO.getCurrentPlayer() == 1) {
            boardList = objectMapper.readValue(gameDTO.getPlayer2Ships(), List.class);
            boardList.add(cell);
            gameDTO.setPlayer2Ships(objectMapper.writeValueAsString(boardList));
        } else {
            boardList = objectMapper.readValue(gameDTO.getPlayer1Ships(), List.class);
            boardList.add(cell);
            gameDTO.setPlayer1Ships(objectMapper.writeValueAsString(boardList));
        }
    }*/

    /* private void setShipHit(GameDTO gameDTO, int x, int y) throws IOException {
        BoardCell[] playerBoard;
        if (gameDTO.getCurrentPlayer() == 1) {
            playerBoard = objectMapper.readValue(gameDTO.getPlayer2Ships(), BoardCell[].class);
            for (BoardCell cell: playerBoard) {
                if (cell.getX() == x && cell.getY() == y) {
                    cell.setValue(-cell.getValue());
                    break;
                }
            }
            gameDTO.setPlayer2Ships(objectMapper.writeValueAsString(playerBoard));
        } else {
            playerBoard = objectMapper.readValue(gameDTO.getPlayer1Ships(), BoardCell[].class);
            for (BoardCell cell: playerBoard) {
                if (cell.getX() == x && cell.getY() == y) {
                    cell.setValue(-cell.getValue());
                    break;
                }
            }
            gameDTO.setPlayer1Ships(objectMapper.writeValueAsString(playerBoard));
        }
    }*/

    //@SuppressWarnings("unchecked")
    /*private void setEnemyBoard(Game game, int x, int y, int value) throws IOException {
        BoardCell[] board;
        BoardCell cell = new BoardCell();
        cell.setX(x);
        cell.setY(y);
        cell.setValue(value);
        if (game.getCurrentPlayer() == 1) {
            if (game.getEnemyBoardForPlayer1JSON() == null){
                board = new BoardCell[1];
                board[0] = cell;
                game.setEnemyBoardForPlayer1JSON(objectMapper.writeValueAsString(board));
            } else {
                List<BoardCell> boardList;
                boardList = objectMapper.readValue(game.getEnemyBoardForPlayer1JSON(), List.class);
                boardList.add(cell);
                game.setEnemyBoardForPlayer1JSON(objectMapper.writeValueAsString(boardList));
            }
        } else {
            if (game.getEnemyBoardForPlayer2JSON() == null){
                board = new BoardCell[1];
                board[0] = cell;
                game.setEnemyBoardForPlayer2JSON(objectMapper.writeValueAsString(board));
            } else {
                List<BoardCell> boardList;
                boardList = objectMapper.readValue(game.getEnemyBoardForPlayer2JSON(), List.class);
                boardList.add(cell);
                game.setEnemyBoardForPlayer2JSON(objectMapper.writeValueAsString(boardList));
            }
        }
    }*/

}
