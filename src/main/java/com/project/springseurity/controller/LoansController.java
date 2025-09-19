package com.project.springseurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {

    @GetMapping(path = "/myLoans")
    public String LoansDetails (){
        return "Here are the Loans related details from db";
    }
}