package com.app.validation;
import com.app.cache.Room;
import com.app.models.BoardCell;
import org.junit.Test;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.UUID;

public class TurnValidatorTest {

    @Test
    public void testNegativeX() {
        Room room = new Room();
        Assert.assertFalse(new TurnValidator().isValidTurn(room, -1, 4, "q"));
    }

    @Test
    public void testNegativeY() {
        Room room = new Room();
        Assert.assertFalse(new TurnValidator().isValidTurn(room, 1, -4, "q"));
    }

    @Test
    public void testOutOfBoundsX() {
        Room room = new Room();
        Assert.assertFalse(new TurnValidator().isValidTurn(room, 10, 4, "q"));
    }

    @Test
    public void testOutOfBoundsY() {
        Room room = new Room();
        Assert.assertFalse(new TurnValidator().isValidTurn(room, 2, 14, "q"));
    }

    @Test
    public void testPositiveCase() {
        Room room = new Room();
        room.setCurrentPlayer(1);
        UUID id = UUID.randomUUID();
        room.setPlayer1Id(id);
        Assert.assertTrue(new TurnValidator().isValidTurn(room, 0, 4, id.toString()));
    }

    @Test
    public void testWrongPlayer() {
        Room room = new Room();
        room.setCurrentPlayer(1);
        UUID id = UUID.randomUUID();
        room.setPlayer1Id(UUID.randomUUID());
        room.setPlayer2Id(id);
        Assert.assertFalse(new TurnValidator().isValidTurn(room, 0, 4, id.toString()));
    }

    @Test
    public void testWhetherCellWasShotBefore() {
        Room room = new Room();
        room.setCurrentPlayer(2);
        UUID id = UUID.randomUUID();
        room.setPlayer1Id(UUID.randomUUID());
        room.setPlayer2Id(id);
        ArrayList<BoardCell> enemyBoard = new ArrayList<>();
        enemyBoard.add(new BoardCell(0,4,-1));
        room.setEnemyBoardForPlayer1(enemyBoard);
        Assert.assertTrue(new TurnValidator().isValidTurn(room, 0, 4, id.toString()));
    }

    @Test
    public void testWhetherCellWasShotBefore2() {
        Room room = new Room();
        room.setCurrentPlayer(2);
        UUID id = UUID.randomUUID();
        room.setPlayer1Id(UUID.randomUUID());
        room.setPlayer2Id(id);
        ArrayList<BoardCell> enemyBoard = new ArrayList<>();
        enemyBoard.add(new BoardCell(0,4,0));
        room.setEnemyBoardForPlayer1(enemyBoard);
        Assert.assertTrue(new TurnValidator().isValidTurn(room, 0, 4, id.toString()));
    }

    @Test
    public void testWhetherCellWasShotBefore3() {
        Room room = new Room();
        room.setCurrentPlayer(2);
        UUID id = UUID.randomUUID();
        room.setPlayer1Id(UUID.randomUUID());
        room.setPlayer2Id(id);
        ArrayList<BoardCell> enemyBoard = new ArrayList<>();
        enemyBoard.add(new BoardCell(0,1,0));
        room.setEnemyBoardForPlayer1(enemyBoard);
        Assert.assertTrue(new TurnValidator().isValidTurn(room, 0, 4, id.toString()));
    }

}
