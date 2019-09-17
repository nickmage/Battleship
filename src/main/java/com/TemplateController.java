package com;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@Controller
public class TemplateController {

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/scoreboard")
    public String scoreboard(){
        return "scoreboard";
    }

    @GetMapping("/any")
    public String any(@RequestHeader("cookie") String cookie) {
        System.out.println(cookie + " from any");
        return "any";
    }

    @GetMapping("/")
    public String index(@RequestHeader("cookie") String cookie/*MultiValueMap<String, String> headers*/) {
        System.out.println(cookie + " from index");
        //headers.forEach((key, value) -> System.out.println(String.format("Header '%s' = %s", key, String.join("|", value))));
        return "index";
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

    @GetMapping("/get_id")
    public String get_id(@RequestHeader("cookie") String cookie) {
        System.out.println(cookie + " get_id");
        return "start";
    }


}
