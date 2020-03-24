package com.example.demo.pojo;

public class PermissionDto {
    private String id;
    private String code;
    private String description;
    private String url;

    public PermissionDto(String id, String code, String description, String url) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.url = url;
    }

    public PermissionDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
