package com.project.springseurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @GetMapping(path = "/myNotification")
    public String myNotifications(){
        return "Here are the Notification related details from db";
    }
}