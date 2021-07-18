package com.example.bk_home_smarter.src.models;

import android.media.Image;

public class Device {
    public String id;
    public String name;
    public String data;
    public String unit;

    public Device(
            // Default object for Firebase to get data
    ){}

    public Device(String id, String name, String data, String unit) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.unit = unit;
    }

    public String toString() {
        return String.format("{ \"id\":\"%s\", \"name\":\"%s\", \"data\":\"%s\", \"unit\":\"%s\"}",
                this.id, this.name, this.data, this.unit);
    }
}
