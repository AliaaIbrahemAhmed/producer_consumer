package com.example.producerConsumer.Services;

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
        messagingTemplate.convertAndSend("/topic/" + topicSuffix, "Default message from our WS service");
    }

    public void sendMessage(final String topicSuffix, final String payload) {
        messagingTemplate.convertAndSend("/topic/" + topicSuffix, payload);
    }
}