package com.example.bk_home_smarter.src.models;

import android.media.Image;

public class Device {
    public String deviceId;
    public String roomId;
    public String deviceName;
    public String deviceType;
    public Boolean deviceStatus;

    public Device(
            // Default object for Firebase to get data
    ){}

    public Device(String deviceId, String roomId, String deviceName, Boolean deviceStatus, String deviceType){
        this.deviceId = deviceId;
        this.roomId = roomId;
        this.deviceName = deviceName;
        this.deviceStatus = deviceStatus;
        this.deviceType = deviceType;
    }
}
