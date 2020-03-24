package com.example.demo.config;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

//@ServerEndpoint(value = "/websocket")
@ServerEndpoint(value = "/websocket/{sessionId}")
@Component
public class CustomWebSocket {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的CumWebSocket对象。
    private static CopyOnWriteArraySet<CustomWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    //存储sessionId
    private static Map<String, Session> sessionPool = new HashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //连接建立成功调用的方法
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "sessionId")String sessionId){
        this.session = session;

        webSocketSet.add(this);
        sessionPool.put(sessionId,session);

        //添加在线人数
        addOnlineCount();
        System.out.println("新连接接入。当前在线人数为：" + getOnlineCount());
    }

    //连接关闭调用的方法
    @OnClose
    public void onClose() {
        //从set中删除
        webSocketSet.remove(this);
        //在线数减1
        subOnlineCount();
        System.out.println("有连接关闭。当前在线人数为：" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用
     *
     * @param message
     *
     */
    @OnMessage
    public void onMessage(String message) {
        System.out.println("客户端发送的消息：" + message);
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        for(CustomWebSocket webSocket : webSocketSet) {
            System.out.println("【websocket消息】广播消息:"+message);
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //发送对象似乎前端接收不到
    public void sendAllMessage(Object object) {
        for(CustomWebSocket webSocket : webSocketSet) {
            System.out.println("【websocket消息】广播消息:"+object);
            try {
               // webSocket.session.getAsyncRemote().sendText(message);
                webSocket.session.getAsyncRemote().sendObject(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String sessionId, String message) {
        Session session = sessionPool.get(sessionId);
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送信息
     *
     * @param message
     * @throws IOException
     */
//    public void sendMessage(String message) throws IOException {
//        //获取session远程基本连接发送文本消息
//        this.session.getBasicRemote().sendText(message);
//        //this.session.getAsyncRemote().sendText(message);
//    }
//
//    /**
//     * 发送信息
//     *
//     * @param object
//     * @throws IOException
//     */
//    public void sendMessage(Object object) throws Exception {
//        //获取session远程基本连接发送文本消息
//        this.session.getBasicRemote().sendObject(object);
//        //this.session.getAsyncRemote().sendText(message);
//    }

    /**
     * 减少在线人数
     */
    private void subOnlineCount() {
        CustomWebSocket.onlineCount--;
    }

    /**
     * 添加在线人数
     */
    private void addOnlineCount() {
        CustomWebSocket.onlineCount++;
    }

    /**
     * 当前在线人数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }


}
