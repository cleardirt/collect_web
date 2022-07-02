package com.example.demo.controller;

import com.example.demo.websocket.WebSocket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webSocket")
public class WebSocketController {

    @GetMapping("/push")
    public void pushOne(){
        WebSocket.sendMessage("hello. this is server.");
    }
}
