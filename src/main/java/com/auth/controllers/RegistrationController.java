package com.auth.controllers;

import com.auth.entities.Role;
import com.auth.entities.User;
import com.auth.repo.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RegistrationController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public ResponseEntity addNewUser(@RequestParam String username, @RequestParam String password,
                                     @RequestParam String confirmPassword) {
        User userFromDB = userRepo.findByUsername(username);
        if (userFromDB != null){
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        else if (username != null && password != null && username.length() >= 3 && password.length() >= 5 &&
                username.length() <= 20 && password.length() <= 20 && password.equals(confirmPassword)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Role.ROLE_USER);
            user.setEnabled(true);
            userRepo.save(user);
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
