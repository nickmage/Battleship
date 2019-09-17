package com;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.stream.Collectors;

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
    public String any(@RequestHeader MultiValueMap<String, String> headers) {
        headers.forEach((key, value) -> System.out.println(String.format("Header '%s' = %s", key, String.join("|", value))));
        //System.out.println(token);
        return "any";
    }

}
