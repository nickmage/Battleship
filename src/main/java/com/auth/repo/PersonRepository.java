package com.auth.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepository {
    private UserRepo userRepo;

    /*public PersonRepository(UserRepo userRepo) {
        this.userRepo = userRepo;
    }*/

    @Autowired
    public UserRepo getUserRepo() {
        return userRepo;
    }
}