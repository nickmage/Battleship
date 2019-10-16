package com.app.response_wrappers;

import java.util.UUID;

public class RoomListResponseWrapper {

    private UUID roomId;
    private String roomName;
    private String player1Name;

    public RoomListResponseWrapper(UUID roomId, String roomName, String player1Name) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.player1Name = player1Name;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

}
