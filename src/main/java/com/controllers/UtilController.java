package com.controllers;

import com.entity.Ship;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UtilController {

    @RequestMapping(value="/start", method = RequestMethod.POST, produces="application/json", consumes = "application/json")
    @ResponseBody
    //@PostMapping("/start")
    public ResponseEntity getShips(@RequestBody() Ship[] ships) {
        //System.out.println(ships);

        return new ResponseEntity(HttpStatus.OK);
        //return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


}
