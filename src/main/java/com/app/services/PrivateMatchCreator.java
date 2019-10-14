package com.app.services;

import com.app.cache.Room;
import com.app.cache.RoomCache;
import com.app.entities.Match;
import com.app.entities.PrivateMatch;
import com.app.models.BoardCell;
import com.app.models.Ship;
import com.app.repo.PrivateMatchRepo;
import com.auth.repo.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class PrivateMatchCreator {
    private final UserRepo userRepo;
    private final PrivateMatchRepo privateMatchRepo;

    public PrivateMatchCreator(UserRepo userRepo, PrivateMatchRepo privateMatchRepo) {
        this.userRepo = userRepo;
        this.privateMatchRepo = privateMatchRepo;
    }

    public ResponseEntity createMatch(String roomName, String password, String username){
        PrivateMatch match = new PrivateMatch();
        match.setRoomName(roomName);
        match.setPassword(password);
        match.setRoomId(UUID.randomUUID());
        match.setDate(new Date());
        match.setPlayer1Id(userRepo.findByUsername(username).getUuid());
        match.setPlayer1Name(username);
        cacheNewPrivateRoom(match);

        privateMatchRepo.save(match);
        return new ResponseEntity<>(match.getRoomId(), HttpStatus.OK);
    }

    private void cacheNewPrivateRoom(PrivateMatch match) {
        Room room = new Room();
        room.setRoomId(match.getRoomId());
        room.setPlayer1Name(match.getPlayer1Name());
        room.setPlayer1Id(match.getPlayer1Id());
        RoomCache.rooms.put(match.getRoomId().toString(), room);
    }

}
