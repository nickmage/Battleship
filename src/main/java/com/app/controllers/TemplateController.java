package com.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class TemplateController {

    @GetMapping("/")
    public String index(@RequestHeader(value = "cookie", required = false) String cookie/*MultiValueMap<String, String> headers*/) {
        //System.out.println(cookie + " from index");
        //headers.forEach((key, value) -> System.out.println(String.format("Header '%s' = %s", key, String.join("|", value))));
        return "forward:/index.html";
    }

    @GetMapping("/login")
    public String login() {
       return "forward:/redirectedLogin.html";
    }

    @GetMapping("/logout")
    public String logout() {
        return "forward:/index.html";
    }

}
