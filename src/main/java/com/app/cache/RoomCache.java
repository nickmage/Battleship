package com.app.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RoomCache {
    public static final HashMap<String, Room> rooms = new HashMap<>();
}
