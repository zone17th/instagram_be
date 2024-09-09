package org.app.instagram_be.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/")
    public String loginTets(){
        return "Hello World - Home";
    }
    @GetMapping("/test")
    public String redirectPage(){
        return "Secure page";
    }
}
