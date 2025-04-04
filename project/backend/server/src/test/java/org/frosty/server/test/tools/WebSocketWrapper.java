package org.frosty.server.test.tools;

import io.micrometer.common.lang.NonNullApi;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class WebSocketWrapper {
    private WebSocketSession session;
    private CompletableFuture<String> messageFuture = new CompletableFuture<>();

    // 封装后API函数1：创建WebSocket连接
    public void connect(String uri) throws ExecutionException, InterruptedException {
        StandardWebSocketClient client = new StandardWebSocketClient();
        CompletableFuture<WebSocketSession> futureSession = client.doHandshake(new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                WebSocketWrapper.this.session = session;
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                // 处理接收到的文本消息
                messageFuture.complete(message.getPayload());
            }
        }, uri, new WebSocketHttpHeaders()).completable();

        this.session = futureSession.get();
    }

    public void sendMessage(String message) throws Exception {
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    public String receiveMessage() throws Exception {
        String rcvd = messageFuture.get();
        messageFuture = new CompletableFuture<>();
        return rcvd;
    }

    public String receiveMessage(int timeout) throws Exception {
        String rcvd = messageFuture.get(timeout, TimeUnit.MILLISECONDS);
        messageFuture = new CompletableFuture<>();
        return rcvd;
    }

    public boolean isOpen() {
        return session != null && session.isOpen();
    }

    public void close() throws Exception {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }
}
