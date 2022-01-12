package com.example.producerConsumer.Configuration;

import com.example.producerConsumer.Model.Connection;
import com.example.producerConsumer.Model.DesignObject;
import com.example.producerConsumer.Model.ResponseObject;
import com.example.producerConsumer.Services.Design;
import com.example.producerConsumer.Services.ResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.util.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;

@CrossOrigin
@RestController
public class Controller {

    @Autowired
    Design design;

    @PostMapping("/setDesign")
    public void setDesign(@RequestBody String designString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DesignObject designObject = mapper.readValue(designString, DesignObject.class);
        design.setDesign(designObject.names, designObject.connections, designObject.products);
    }
  /*  @PostMapping("/setDesign")
    public void setDesign(@RequestParam ArrayList<String> names, @RequestBody ArrayList<Connection> connections, @RequestParam ArrayList<String> products) {
        design.setDesign(names, connections, products);
    }*/

 /*   @PostMapping("getChange")
    public ResponseObject getChange(){
        return ResponseService.pop();
    }*/

    @MessageMapping("/messages")
    @SendTo("/topic/messages")
    public static ResponseObject send(ResponseObject responseObject) throws InterruptedException {
        System.out.println("HI");
        Thread.sleep(1000);
        return responseObject;
    }

}
