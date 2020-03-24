package com.example.demo.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Trace")
public class Trace {

    @Id
    private String id;

    private GeoJsonLineString geoJsonLineString;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GeoJsonLineString getGeoJsonLineString() {
        return geoJsonLineString;
    }

    public void setGeoJsonLineString(GeoJsonLineString geoJsonLineString) {
        this.geoJsonLineString = geoJsonLineString;
    }
}
