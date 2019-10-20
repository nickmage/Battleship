package com.app.validation;

import com.app.models.Ship;
import org.junit.Test;

import org.junit.Assert;

public class BoardValidationTest {

    @Test
    public void testPositiveCase() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                new Ship(2, 5, 'v', 2),
                new Ship(2, 8, 'h', 2),
                new Ship(5, 1, 'v', 3),
                new Ship(8, 4, 'h', 3),
                new Ship(4, 8, 'v', 4),
                new Ship(0, 7, '-', 1),
                new Ship(5, 3, '-', 1),
                new Ship(9, 9, '-', 1),
                new Ship(3, 1, '-', 1)};
        Assert.assertTrue(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testNull() {
        Assert.assertFalse(new BoardValidator(null).isValidBoard());
    }

    @Test
    public void testInsufficientShips() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                new Ship(2, 5, 'v', 2),
                new Ship(2, 8, 'h', 2),
                new Ship(3, 1, '-', 1)};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testInsufficientShipsWithNull() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                null,
                new Ship(2, 5, 'v', 2),
                new Ship(2, 8, 'h', 2),
                new Ship(3, 1, '-', 1)};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testShipsWithNulls() {
        Ship[] ships = {null, null, null, null, null, null, null, null, null, null};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testIncorrectDeckType() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                new Ship(2, 5, 'v', 2),
                new Ship(2, 8, 'h', 2),
                new Ship(5, 1, 'v', 5),
                new Ship(8, 4, 'h', 3),
                new Ship(4, 8, 'v', 4),
                new Ship(0, 7, '-', 1),
                new Ship(5, 3, '-', 1),
                new Ship(9, 9, '-', 1),
                new Ship(3, 1, '-', 1)};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testIncorrectDeckType1Quantity() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                new Ship(2, 5, 'v', 2),
                new Ship(2, 8, 'h', 2),
                new Ship(5, 1, 'v', 3),
                new Ship(8, 4, 'h', 1),
                new Ship(4, 8, 'v', 4),
                new Ship(0, 7, '-', 1),
                new Ship(5, 3, '-', 1),
                new Ship(9, 9, '-', 1),
                new Ship(3, 1, '-', 1)};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testIncorrectDeckType2Quantity() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                new Ship(2, 5, 'v', 2),
                new Ship(2, 8, 'h', 2),
                new Ship(5, 1, 'v', 3),
                new Ship(8, 4, 'h', 3),
                new Ship(4, 8, 'v', 4),
                new Ship(0, 7, '-', 2),
                new Ship(5, 3, '-', 1),
                new Ship(9, 9, '-', 1),
                new Ship(3, 1, '-', 1)};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testIncorrectDeckType3Quantity() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                new Ship(2, 5, 'v', 2),
                new Ship(2, 8, 'h', 3),
                new Ship(5, 1, 'v', 3),
                new Ship(8, 4, 'h', 3),
                new Ship(4, 8, 'v', 4),
                new Ship(0, 7, '-', 1),
                new Ship(5, 3, '-', 1),
                new Ship(9, 9, '-', 1),
                new Ship(3, 1, '-', 1)};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testIncorrectDeckType4Quantity() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                new Ship(2, 5, 'v', 2),
                new Ship(2, 8, 'h', 2),
                new Ship(5, 1, 'v', 3),
                new Ship(8, 4, 'h', 4),
                new Ship(4, 8, 'v', 4),
                new Ship(0, 7, '-', 1),
                new Ship(5, 3, '-', 1),
                new Ship(9, 9, '-', 1),
                new Ship(3, 1, '-', 1)};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testShipTypeIsMissing() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                new Ship(2, 5, 'v', 2),
                new Ship(2, 8, 'h', 2),
                new Ship(5, 1, 'v', 3),
                new Ship(8, 4, 'h', 3),
                new Ship(4, 8, 'v', 0),
                new Ship(0, 7, '-', 1),
                new Ship(5, 3, '-', 1),
                new Ship(9, 9, '-', 1),
                new Ship(3, 1, '-', 1)};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testNineShips() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                new Ship(2, 5, 'v', 2),
                new Ship(2, 8, 'h', 2),
                new Ship(5, 1, 'v', 3),
                new Ship(8, 4, 'h', 3),
                new Ship(0, 7, '-', 1),
                new Ship(5, 3, '-', 1),
                new Ship(9, 9, '-', 1),
                new Ship(3, 1, '-', 1)};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testCollisionOverlay() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                new Ship(1, 1, 'v', 2),
                new Ship(2, 8, 'h', 2),
                new Ship(5, 1, 'v', 3),
                new Ship(8, 4, 'h', 3),
                new Ship(4, 8, 'v', 4),
                new Ship(0, 7, '-', 1),
                new Ship(5, 3, '-', 1),
                new Ship(9, 9, '-', 1),
                new Ship(3, 1, '-', 1)};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testCollision() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                new Ship(2, 1, 'v', 2),
                new Ship(2, 8, 'h', 2),
                new Ship(5, 1, 'v', 3),
                new Ship(8, 4, 'h', 3),
                new Ship(4, 8, 'v', 4),
                new Ship(0, 7, '-', 1),
                new Ship(5, 3, '-', 1),
                new Ship(9, 9, '-', 1),
                new Ship(3, 1, '-', 1)};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

    @Test
    public void testCollisionStandingNearby() {
        Ship[] ships = {new Ship(1, 1, 'h', 2),
                new Ship(2, 5, 'v', 2),
                new Ship(2, 8, 'h', 2),
                new Ship(5, 1, 'v', 3),
                new Ship(8, 4, 'h', 3),
                new Ship(4, 8, 'v', 4),
                new Ship(0, 7, '-', 1),
                new Ship(0, 8, '-', 1),
                new Ship(9, 9, '-', 1),
                new Ship(3, 1, '-', 1)};
        Assert.assertFalse(new BoardValidator(ships).isValidBoard());
    }

}
