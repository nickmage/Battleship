package com.app.services;

import com.app.cache.Room;
import com.app.cache.RoomCache;
import org.springframework.stereotype.Service;

@Service
public class GameFinder {

    public Room findGameInCache(String roomId){
        return RoomCache.rooms.get(roomId);
    }

}
