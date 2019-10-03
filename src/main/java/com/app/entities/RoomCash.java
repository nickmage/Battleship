package com.app.entities;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RoomCash {
    public static HashMap<String, Room> rooms = new HashMap<>();
}
