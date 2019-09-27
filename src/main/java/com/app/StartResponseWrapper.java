package com.app;

public class StartResponseWrapper {
    private String playerId;
    private String roomId;

    public StartResponseWrapper(String playerId, String roomId) {
        this.playerId = playerId;
        this.roomId = roomId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
