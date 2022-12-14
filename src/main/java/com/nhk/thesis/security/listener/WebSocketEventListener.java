package com.nhk.thesis.security.listener;

import com.nhk.thesis.controller.WebSocketController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);


    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Receive a new websocket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        logger.info(username + " someone has disconnected!");

        if(username != null) {
            logger.info("User Disconnected : " + username);

            WebSocketController.activeUser.remove(username);

            WebSocketController.simpMessagingTemplate.convertAndSend("/topic/disconnect", username);
        }
    }

}
