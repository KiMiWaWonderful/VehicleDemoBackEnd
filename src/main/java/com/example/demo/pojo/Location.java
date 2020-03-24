package com.example.demo.pojo;

public class Location {

    //纬度
    double lat;
    //经度
    double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Location() {
    }

    //    String lat;
//    String lng;
//
//    public String getLat() {
//        return lat;
//    }
//
//    public void setLat(String lat) {
//        this.lat = lat;
//    }
//
//    public String getLng() {
//        return lng;
//    }
//
//    public void setLng(String lng) {
//        this.lng = lng;
//    }
//
//    public Location(String lat, String lng) {
//        this.lat = lat;
//        this.lng = lng;
//    }
//
//    public Location() {
//    }
//
//    @Override
//    public String toString() {
//        return "Location{" +
//                "latitude='" + lat + '\'' +
//                ", longitude='" + lng + '\'' +
//                '}';
//    }
}
