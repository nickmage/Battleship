package com.app.entities;

import java.util.UUID;

public interface Matchmaking {

    UUID getRoomId();

    String getPlayer1Name();

    UUID getPlayer1Id();

    String getPlayer2Name();

    UUID getPlayer2Id();

    String getPlayer1Ships();

    String getPlayer2Ships();

}
