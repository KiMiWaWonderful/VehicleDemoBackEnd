package com.example.demo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

// 栅栏
@Document(collection="fence")
public class Fence implements Serializable {
    @Id
    private String id;

    private List<Point> coordinates;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createtime;

    public Fence() {
    }

    public Fence(String id, List<Point> coordinates, Date createtime) {
        this.id = id;
        this.coordinates = coordinates;
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "Fence{" +
                "id='" + id + '\'' +
                ", coordinates=" + coordinates +
                ", createtime=" + createtime +
                '}';
    }

    public List<Point> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Point> coordinates) {
        this.coordinates = coordinates;
    }

    public Date getcreatetime() {
        return createtime;
    }

    public void setcreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
