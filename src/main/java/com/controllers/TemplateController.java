package com.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class TemplateController {

    @GetMapping("/angular")
    public String angular(){
        return "angular";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @GetMapping("/scoreboard")
    public String scoreboard(){
        return "scoreboard";
    }

    @GetMapping("/menu")
    public String any(@RequestHeader("cookie") String cookie) {
        System.out.println(cookie + " from menu");
        return "menu";
    }

    @GetMapping("/")
    public String index(@RequestHeader(value = "cookie", required = false) String cookie/*MultiValueMap<String, String> headers*/) {
        System.out.println(cookie + " from index");
        //headers.forEach((key, value) -> System.out.println(String.format("Header '%s' = %s", key, String.join("|", value))));
        return "redirect:/index.html";
    }

    @GetMapping("/start")
    public String start(@RequestHeader("cookie") String cookie) {
        System.out.println(cookie + " from start");
        return "start";
    }

    @GetMapping("/lobby")
    public String lobby(@RequestHeader("cookie") String cookie) {
        System.out.println(cookie + " from lobby");
        return "lobby";
    }

    @GetMapping("/game")
    public String game(@RequestHeader("cookie") String cookie) {
        System.out.println(cookie + " from game");
        return "game";
    }

}
