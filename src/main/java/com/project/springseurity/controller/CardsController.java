package com.project.springseurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardsController {

    @GetMapping(path = "/myCards")
    public String cardsDetails(){
        return "Here are the Cards related details from db";
    }
}