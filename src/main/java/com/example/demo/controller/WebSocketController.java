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

     //   Random random = new Random();
       // final int[] i = {0};

//        Socket socket = new Socket("111.230.213.172",9000);
//        InputStream inputStream = socket.getInputStream();
//        while (true){
//            int count = 0;
//            while (count == 0) {
//                count = inputStream.available();
//            }
//            System.out.println("能读的大小:"+count + "");
//            //   Log.e("能读的大小", count + "");
//            byte[] b = new byte[count];
//            inputStream.read(b);
//            String strRead = new String(b);
//            strRead = String.copyValueOf(strRead.toCharArray(), 0, b.length);
//            System.out.println(strRead);
//
//            String[] strings = strRead.split(",");
//            double lng = Double.parseDouble(strings[1]);
//            double lat = Double.parseDouble(strings[2]);
//
//            Location location = new Location(lng,lat);
//            webSocket.sendOneMessage("location", JsonUtils.objectToJson(location));
//        }

        //使用geojson
        Trace trace = new Trace();
        List<Point> pointList = new ArrayList<>();
        pointList.add(new Point(110.92007160186768,21.65830568281311));
        pointList.add(new Point(110.92007160186768, 21.65802648347845));
        pointList.add(new Point(110.92002868652344,21.65786694075894));
        pointList.add(new Point(110.92002868652344,21.657587740575302));
        pointList.add(new Point( 110.92007160186768,21.657228768116763));
        pointList.add(new Point(110.92002868652344,21.65690968073723));
        pointList.add(new Point(110.92071533203125,21.65667036473951));

        pointList.add(new Point(110.92118740081787, 21.656510820520534));
        pointList.add(new Point(110.92002868652344,21.65603218680517));
        pointList.add(new Point(110.92002868652344,21.6554338924284));
        pointList.add(new Point(110.92002868652344, 21.65495525514098));
        pointList.add(new Point(110.9203290939331,21.654556389522075));

        pointList.add(new Point(110.92114448547363, 21.65435695629922));
        pointList.add(new Point(110.92105865478516,21.654077749324184));
        pointList.add(new Point(110.92041492462158,21.653838428629932));
        pointList.add(new Point(110.9203290939331, 21.65351933375358));
        pointList.add(new Point(110.92028617858887,21.653160351174346));

        pointList.add(new Point(110.9203290939331,21.652721593476006));
        pointList.add(new Point(110.92024326324463,21.6523626089125));
        pointList.add(new Point( 110.9203290939331,21.65160474967952));
        pointList.add(new Point(110.92062950134277, 21.65148508733159));
        pointList.add(new Point(110.92105865478516,21.651205874800645));

        pointList.add(new Point(110.92157363891602,21.65100643694783));
        pointList.add(new Point(110.92191696166992,21.6509665493442));
        pointList.add(new Point( 110.92247486114502,21.650647448118338));
        pointList.add(new Point(110.92294692993164,21.650487897240858));
        pointList.add(new Point(110.92333316802979,21.650168794956798));

        pointList.add(new Point(110.92363357543945,21.649650252240637));
        pointList.add(new Point( 110.92363357543945,21.6494109242049));
        pointList.add(new Point(110.92359066009521,21.649211483872005));
        pointList.add(new Point(110.92359066009521,21.649012043263546));
//        GeoJsonLineString geoJsonLineString = new GeoJsonLineString(pointList);
//        trace.setGeoJsonLineString(geoJsonLineString);
        // traceRepository.save(trace);
        for (int i = 0; i < pointList.size(); i++) {
            Thread.sleep(4000);
            webSocket.sendOneMessage("location", JSON.toJSONString(pointList.get(i)));
        }

//        new Thread() {
//            double i = 0;
//            @Override
//            public void run() {
//                while (true) {
//
//                    try {
//
//
//
//
//
//                      //  int i = random.nextInt(10);
//                        Location location = new Location(39.930 + i,116.404);
//                        //webSocket.sendAllMessage(JsonUtils.objectToJson(location));
//                        webSocket.sendOneMessage("location", JsonUtils.objectToJson(location));
////                        i[0] = (int) (i[0] + 0.5);
////                        System.out.println(i[0]);
//                        i = i + 0.003;
//                        System.out.println(i);
//
//                        sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();



//        Location location = new Location("40.7838351","-74.6143763");
  //      Location location = new Location(39.930,119.404);
//        webSocket.sendAllMessage(location);
//        webSocket.sendAllMessage(location.toString());
   //     webSocket.sendAllMessage(JsonUtils.objectToJson(location));
       // webSocket.sendMessage(location);
       // webSocket.sendAllMessage("清晨起来打开窗，心情美美哒~");
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
