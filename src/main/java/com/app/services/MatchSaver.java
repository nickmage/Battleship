package com.app.services;

import com.app.entities.Match;
import com.app.models.Ship;
import com.app.repo.GameRepo;
import com.app.repo.MatchRepo;
import com.app.response_wrappers.StartResponseWrapper;
import com.app.validation.BoardValidator;
import com.auth.entities.User;
import com.auth.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Scope(value="prototype", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class MatchSaver {

    private final MatchCreator matchCreator;
    private final GameRepo gameRepo;
    private final GameCreator gameCreator;
    private final UserRepo userRepo;
    private final MatchRepo matchRepo;

    public MatchSaver(MatchCreator matchCreator, GameRepo gameRepo, GameCreator gameCreator, UserRepo userRepo, MatchRepo matchRepo) {
        this.matchCreator = matchCreator;
        this.gameRepo = gameRepo;
        this.gameCreator = gameCreator;
        this.userRepo = userRepo;
        this.matchRepo = matchRepo;
    }

    public ResponseEntity getResponse(Ship[] ships, String username) throws JsonProcessingException {
        return  null;}
}
