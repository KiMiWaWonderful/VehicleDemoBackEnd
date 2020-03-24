package com.example.demo.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Point implements Serializable {

    private double lng;
    private double lat;

    public Point(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }
}
