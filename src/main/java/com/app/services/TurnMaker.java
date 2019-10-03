package com.app.services;

import com.app.DTOs.Game;
import com.app.entities.BoardCell;
import com.app.repo.GameRepo;
import com.app.response_wrappers.ShotResponseWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class TurnMaker {
    /*@Autowired
    private ShotResponseWrapper shotResponseWrapper;*/
    @Autowired
    private GameRepo gameRepo;
    @Autowired
    private ObjectMapper objectMapper;

    public ShotResponseWrapper makeShot(Game game, int x, int y) throws IOException {
        ShotResponseWrapper response = new ShotResponseWrapper();
       // execute(game, x, y);
        if (game.getCurrentPlayer() == 1) {
           // response.setEnemyBoard(objectMapper.readValue(game.getEnemyBoardForPlayer1JSON(), BoardCell[].class));
            //response.setEnemyShips(objectMapper.readValue(game.getRemainingShipsOfPlayer1JSON(), RemainingShips.class));
            response.setMyTurn(game.getCurrentPlayer() == 1);
            response.setWinner(game.getWinner());
        } else {
         //   response.setEnemyBoard(objectMapper.readValue(game.getEnemyBoardForPlayer2JSON(), BoardCell[].class));
           // response.setEnemyShips(objectMapper.readValue(game.getRemainingShipsOfPlayer1JSON(), RemainingShips.class));
            response.setMyTurn(game.getCurrentPlayer() == 2);
            response.setWinner(game.getWinner());
        }
        game.setCurrentPlayer((game.getCurrentPlayer() == 1) ? 2 : 1);
        /**isWinner();*/
        game.setDate(new Date());
        /**game.setId(game.getId() + 1);*/
        gameRepo.save(game);

        return response;
    }

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

    @SuppressWarnings("unchecked")
    private void setShipMiss(Game game, int x, int y) throws IOException {
        List<BoardCell> boardList;
        BoardCell cell = new BoardCell();
        cell.setX(x);
        cell.setY(y);
        cell.setValue(0);
        if (game.getCurrentPlayer() == 1) {
            boardList = objectMapper.readValue(game.getPlayer2Board(), List.class);
            boardList.add(cell);
            game.setPlayer2Board(objectMapper.writeValueAsString(boardList));
        } else {
            boardList = objectMapper.readValue(game.getPlayer1Board(), List.class);
            boardList.add(cell);
            game.setPlayer1Board(objectMapper.writeValueAsString(boardList));
        }
    }

    //returns 0 if miss or negative value in case of hit
    private int isShipHit(BoardCell[][] ships, int x, int y) {
        for (BoardCell[] ship : ships) {
            for (BoardCell cell : ship) {
                if (cell.getX() == x && cell.getY() == y) {
                    cell.setValue(-cell.getValue());
                    if (isShipSunken(ship)) {
                        System.out.println("Sunken");
                        /**setEnemyBoard(game, x, y);*/
                    }
                    return cell.getValue();
                }
            }
        }
        return 0;
    }

    private boolean isShipSunken(BoardCell[] ship) {
        int count = 0;
        for (BoardCell boardCell : ship) {
            if (boardCell.getValue() < 0) {
                count++;
            }
        }
        return count == ship.length;
    }

    private void setShipHit(Game game, int x, int y) throws IOException {
        BoardCell[] playerBoard;
        if (game.getCurrentPlayer() == 1) {
            playerBoard = objectMapper.readValue(game.getPlayer2Board(), BoardCell[].class);
            for (BoardCell cell: playerBoard) {
                if (cell.getX() == x && cell.getY() == y) {
                    cell.setValue(-cell.getValue());
                    break;
                }
            }
            game.setPlayer2Board(objectMapper.writeValueAsString(playerBoard));
        } else {
            playerBoard = objectMapper.readValue(game.getPlayer1Board(), BoardCell[].class);
            for (BoardCell cell: playerBoard) {
                if (cell.getX() == x && cell.getY() == y) {
                    cell.setValue(-cell.getValue());
                    break;
                }
            }
            game.setPlayer1Board(objectMapper.writeValueAsString(playerBoard));
        }
    }

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
