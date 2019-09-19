package com.controllers;


import com.entity.Ship;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UtilController {

    @GetMapping("/get_cookie")
    public String get_cookie(@RequestHeader("cookie") String cookie) {
        System.out.println(cookie + " from get_cookie");
        return "/menu";
    }

    /*@PostMapping("/registration")
    public String registration(User user, Model model) {
        //System.out.println(cookie + "from registration");
        User userFromDB = userRepo.findByUsername(user.getUsername());
        if (userFromDB != null){
            model.addAttribute("message", "This user already exists!");
            return "/registration";
        } else {
            user.setRole(Collections.singleton(Role.ROLE_USER));
            userRepo.save(user);

            return "/login";
        }

    }*/

    @RequestMapping(value="/start", method = RequestMethod.POST, produces="application/json", consumes = "application/json")
    @ResponseBody
    //@PostMapping("/start")
    public ResponseEntity registration(@RequestBody() Ship[] ships) {
        //System.out.println(ships);

        return new ResponseEntity(HttpStatus.OK);
        //return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


}
