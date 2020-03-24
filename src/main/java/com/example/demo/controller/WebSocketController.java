package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.config.CustomWebSocket;
import com.example.demo.mapper.TraceRepository;
import com.example.demo.pojo.CarData;
import com.example.demo.pojo.Location;
import com.example.demo.pojo.Point;
import com.example.demo.pojo.Trace;
import com.example.demo.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.geo.Point;
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

    @RequestMapping("/sendLocationData")
    public String testLocationData() throws Exception{

        // 开一个线程负责接收服务器端的消息
        new Thread(){
            @Override
            public void run() {
                Socket socket = null;
                try {
                    // 连接服务器端
                    socket = new Socket("127.0.0.1",9001);
                    // 获取流对象
                    InputStream is = socket.getInputStream();

                    byte[] bytes = new byte[1024];
                    int len = 0;
                    // 服务器定时发送数据，客户端不断接受
                    while((len = is.read(bytes)) != -1) {
                        // 处理数据
                        String data = new String(bytes, 0, len);
                        String[] strings = data.split(",");
                        double lng = Double.parseDouble(strings[0]);
                        double lat = Double.parseDouble(strings[1]);
                        Point point = new Point(lng,lat);
                        // 往浏览器发送数据
                        webSocket.sendOneMessage("location", JSON.toJSONString(point));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

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
