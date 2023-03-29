package com.example.nettalk.hanlder;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
//@ServerEndpoint("/websocket")
public class ChatHandler extends TextWebSocketHandler {
    private List<WebSocketSession> list = new ArrayList<>();

    @Override
    @OnMessage
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload : " + payload);

        for(WebSocketSession sess: list) {
            sess.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        list.add(session);

        log.info(session + "클라이언트 접속");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(session + "클라이언트 접속 해제");
        list.remove(session);
    }
}
