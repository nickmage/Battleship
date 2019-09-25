package com.app.controllers;

import com.app.entity.Ship;
import com.app.validation.BoardValidator;
import com.auth.entities.User;
import com.auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class GameController {
    @Autowired
    private UserRepo userRepo;

    @RequestMapping(value = "/start", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> getShips(@RequestBody Ship[] ships,
                                           @RequestHeader(name = "Username")String username) {
        System.out.println(username);
        if (new BoardValidator(ships).isValidBoard()) {
            User userFromDB = userRepo.findByUsername(username);
            if(userFromDB != null){
                String id = userFromDB.getUuid().toString();
                return new ResponseEntity<>(id, HttpStatus.OK);
            } else return new ResponseEntity<>("Cannot find your account! Please reenter the game and try again!", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("An error has occurred (the board is invalid)! Please try again and send a valid board.", HttpStatus.BAD_REQUEST);
    }


}
