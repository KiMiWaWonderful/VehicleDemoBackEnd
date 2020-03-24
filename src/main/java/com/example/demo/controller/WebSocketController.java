package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.config.CustomWebSocket;
import com.example.demo.mapper.TraceRepository;
import com.example.demo.netty.NettyClient;
import com.example.demo.netty.NettyClientHandler;
import com.example.demo.pojo.CarData;
import com.example.demo.pojo.Location;
import com.example.demo.pojo.Point;
import com.example.demo.pojo.Trace;
import com.example.demo.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.geo.Point;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ws")
public class WebSocketController {
    @Autowired
    private CustomWebSocket webSocket;

    @Autowired
    private TraceRepository traceRepository;

    @Autowired
    NettyClient nettyClient;


    @RequestMapping("/sendLocationData")
    public String testLocationData() throws Exception{

        // 开一个线程负责接收服务器端的消息，并且发送给浏览器
        new Thread(nettyClient).run();

        return "websocket群体发送LocationData！";
    }

    @RequestMapping("/sendCarData")
    public String testCarData() throws Exception{
        CarData carData = new CarData(26,0.0001,2.344444,0.56);
        //webSocket.sendAllMessage(JsonUtils.objectToJson(carData));
        webSocket.sendOneMessage("carData",JSON.toJSONString(carData));
        return "websocket群体发送CarData！";
    }

    //发送center坐标
    @RequestMapping("/sendCenter")
    @CrossOrigin
    public ResponseResult<Location> testCenter() throws Exception{
        Location location = new Location(21.661895340414247,110.91608047485352);
        return new ResponseResult<>(HttpStatus.OK.value(),"",location);
    }


}
