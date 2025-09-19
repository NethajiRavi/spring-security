package com.project.springseurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    @GetMapping(path = "/myAccount")
    public String getAccountDetails(){
        return "Here are the account related details from db";
    }
}