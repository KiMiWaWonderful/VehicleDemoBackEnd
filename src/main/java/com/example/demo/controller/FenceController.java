package com.example.demo.controller;

import com.example.demo.pojo.Fence;
import com.example.demo.pojo.Point;
import com.example.demo.service.FenceService;
import com.example.demo.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class FenceController {

    private String lastValue = "";

    @Autowired
    FenceService fenceService;

    @Autowired
    private DataSource dataSource;

    @GetMapping("check")
    public ResponseResult check(Point point) {

        // 模拟一个栅栏进行测试
        List<Point> region = new ArrayList<>();
        Point point1 = new Point(110.913432,21.676292); region.add(point1);
        Point point2 = new Point(110.907755,21.674478); region.add(point2);
        Point point3 = new Point(110.90912,21.669708); region.add(point3);
        Point point4 = new Point(110.91645,21.66729); region.add(point4);
        Point point5 = new Point(110.92069,21.669574); region.add(point5);
        Point point6 = new Point(110.921122,21.674008); region.add(point6);
        Point point7 = new Point(110.915013,21.67656); region.add(point7);

        boolean flag = false;   //偶数次在外面，奇数次在里面
        double px = point.getLng(), py = point.getLat();

//        System.out.println(point.toString());

        for(int i = 0; i < region.size(); i++) {
            int j = i+1;
            //实际上就是每一条边都得算一下，首尾也有一条边得算在内
            if(i == region.size() - 1) {
                j = 0;
            }
            //取相邻的两个点
            double sx = region.get(i).getLng(), sy = region.get(i).getLat(), tx = region.get(j).getLng(), ty = region.get(j).getLat();

            //判断一下点是不是和多边形的顶点重合
            if((sx == px && sy == py) || (tx == px && ty == py)) {
                System.out.println("在边界上");
//                return "on";
            }
            //不在顶点的话，判断一下是不是就在当前线上
            //做一条包含P点的，平行于y轴的直线
            //首先保证线段端点在射线两侧，和P点不是平行
            if((sy < py && ty >= py) || (sy >= py && ty < py)) {

                double diff = (px - sx) * (ty - sy) - (py - sy) * (tx - sx);
                //判断一下px点是不是在边界上
                if(diff == 0) {
                    System.out.println("在边界上");
//                    return "on";
                }

                else{
                    //求一下交点
                    double x = sx + (py - sy) * (tx - sx) / (ty - sy);
                    //射线只往右边走
                    //判断一下交点是否在当前点的右侧
                    if(px > x) {
                        flag = !flag;
                    }
                }
            }
        }

//        if(flag){
//            System.out.println("在里面");
//        } else {
//            System.out.println("在外面");
//        }

        if(lastValue == ""){
            lastValue = String.valueOf(flag);
        } else {
            // 判断与上次的状态是否不同
            if(Boolean.parseBoolean(lastValue) ^ flag){
                if(flag){
                    System.out.println("进去了");
                    lastValue = String.valueOf(flag);
                    return new ResponseResult<>(HttpStatus.OK.value(),"in","in");
                } else {
                    System.out.println("出来了");
                    lastValue = String.valueOf(flag);
                    return new ResponseResult<>(HttpStatus.OK.value(),"out","out");
                }
            }
        }

        return new ResponseResult<>(HttpStatus.OK.value(),"nochange","nochange");
    }

    @GetMapping("fence")
    public ResponseResult getAllFence() throws SQLException {

        List<Fence> fenceList = fenceService.getAllFence();

        System.out.println(dataSource.getConnection());

        return new ResponseResult<>(HttpStatus.OK.value(),fenceList);
    }

    @PostMapping("fence")
    public ResponseResult addFence(@RequestBody Fence fence){

        fenceService.addFence(fence);

        return new ResponseResult<>(HttpStatus.OK.value(),"success");
    }

    @GetMapping("fence/{id}")
    public ResponseResult getFenceById(@PathVariable("id") String id){

        Fence fence = fenceService.getFenceById(id);

        return new ResponseResult<>(HttpStatus.OK.value(),fence);
    }

    @DeleteMapping("fence/{id}")
    public ResponseResult deleteFenceById(@PathVariable("id") String id){

        fenceService.deleteFenceById(id);

        return new ResponseResult<>(HttpStatus.OK.value(),"success");
    }


}
