package com.project.springseurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    @GetMapping(path = "/myContact")
    public String fetchMyContact(){
        return "Here are the contact related details from db";
    }
}