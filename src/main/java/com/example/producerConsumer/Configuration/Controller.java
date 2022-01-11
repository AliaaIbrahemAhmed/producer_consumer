package com.example.producerConsumer.Configuration;

import com.example.producerConsumer.Model.Connection;
import com.example.producerConsumer.Model.ResponseObject;
import com.example.producerConsumer.Services.Design;
import com.example.producerConsumer.Services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;

@CrossOrigin
@RestController
public class Controller {

    @Autowired
    Design design;

    @GetMapping("/setDesign")
    public void setDesign(@RequestParam ArrayList<String> names, @RequestBody ArrayList<Connection> connections, @RequestParam ArrayList<String> products) {
        design.setDesign(names, connections, products);
    }

    @PostMapping("getChange")
    public ResponseObject getChange(){
        return ResponseService.pop();
    }

    @MessageMapping("/messages")
    @SendTo("topic/messages")
    public static ResponseObject send(ResponseObject responseObject) throws InterruptedException {
        Thread.sleep(1000);
        return responseObject;
    }

}
