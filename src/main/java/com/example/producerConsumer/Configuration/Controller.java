package com.example.producerConsumer.Configuration;

import com.example.producerConsumer.Model.DesignObject;
import com.example.producerConsumer.Model.ResponseObject;
import com.example.producerConsumer.Services.Design;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/replay")
    public void replay() {
        design.replay();
    }

    @MessageMapping("/messages")
    @SendTo("/topic/messages")
    public static ResponseObject send(ResponseObject responseObject) throws InterruptedException {
        System.out.println("HI");
        Thread.sleep(1000);
        return responseObject;
    }

}
