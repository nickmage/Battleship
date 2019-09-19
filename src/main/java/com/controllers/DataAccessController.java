package com.controllers;

import com.auth.entities.User;
import com.auth.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataAccessController {
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/registration")
    public ResponseEntity addNewUser(@RequestParam String username, @RequestParam String password,
                                     @RequestParam String confirmPassword) {
        System.out.println(username + password + confirmPassword);
        if (password.equals(confirmPassword) && username.length() >= 3 && password.length() >= 3) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            userRepo.save(user);
            return new ResponseEntity(HttpStatus.OK);
        } else return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    public
    Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepo.findAll();
    }
}
