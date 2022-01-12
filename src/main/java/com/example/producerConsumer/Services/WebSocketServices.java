package com.example.producerConsumer.Services;

import com.example.producerConsumer.Model.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketServices {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketServices(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(final String topicSuffix) {
        messagingTemplate.convertAndSend("/topic/messages" + topicSuffix, "Default message from our WS service");
    }

    public void sendMessage(final String topicSuffix, final ResponseObject payload) {
        messagingTemplate.convertAndSend("/topic/" + topicSuffix, payload);
    }
}