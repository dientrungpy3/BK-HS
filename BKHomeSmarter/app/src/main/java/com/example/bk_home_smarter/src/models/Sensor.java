package com.example.bk_home_smarter.src.models;

public class Sensor {
    public String sensorId;
    public String roomId;
    public String sensorName;
    public String sensorInfo;

    public Sensor(){

    }

    public Sensor(String sensorId, String roomId, String sensorName, String sensorInfo) {
        this.sensorId = sensorId;
        this.roomId = roomId;
        this.sensorName = sensorName;
        this.sensorInfo = sensorInfo;
    }
}
