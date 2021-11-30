package com.example.carbonfootprints;

public class TimeLineModel {
    private String name;
    private String status;
    private String description;

    public TimeLineModel() {
    }

    public TimeLineModel(String name, String status, String description) {
        this.name = name;
        this.status = status;
        this.description = description;
    }

    String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

    String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}