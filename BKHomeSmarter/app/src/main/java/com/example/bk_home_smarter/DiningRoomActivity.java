package com.example.bk_home_smarter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bk_home_smarter.src.models.Device;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.example.bk_home_smarter.MQTTService;

import java.nio.charset.Charset;

public class DiningRoomActivity extends AppCompatActivity {

    MQTTService mqttService;
    DatabaseReference mData;

    Device tem_hum_sensor = new Device("7", "TEMP-HUMID", "0-0", "*C-%");
    Device fan = new Device("11", "RELAY", "0", "");
    Device light = new Device("31", "RELAY", "0", "");
    Device air = new Device("32", "RELAY", "0", "");
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_room);

        // Create view
        TextView temp = findViewById(R.id.txt_dining_temp);
        TextView hum = findViewById(R.id.txt_dining_hum);
        Switch switch_fan = findViewById(R.id.fan_dining_switch);
        Switch switch_light = findViewById(R.id.light_dining_switch);
        Switch switch_air = findViewById(R.id.air_dining_switch);
        boolean switch_fan_state = false;
        boolean switch_light_state = false;
        boolean switch_air_state = false;

        ImageView fan_img = (ImageView) findViewById(R.id.fan_dining);
        ImageView air_img = (ImageView) findViewById(R.id.air_dining);
        ImageView light_img = (ImageView) findViewById(R.id.light_dining);

        // Create device
        mData = FirebaseDatabase.getInstance().getReference();

        // Update last data
        mData.child("Device").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
//                String last_temp = snapshot.toString();
//                Toast.makeText(getApplicationContext(), last_temp, Toast.LENGTH_SHORT).show();

                // Get last temp and hum
                String last_temp = snapshot.child(tem_hum_sensor.id).getValue().toString().split("-")[0];
                String last_hum = snapshot.child(tem_hum_sensor.id).getValue().toString().split("-")[1];

                System.out.println(snapshot.getValue());
                temp.setText(last_temp + "\u00B0" + "C");
                hum.setText(last_hum + "%");

                // Get last fan status
                String fan_status = snapshot.child(fan.id).getValue().toString();
                boolean switch_fan_state = fan_status.equals("1");
                switch_fan.setChecked(switch_fan_state);
                if (switch_fan_state) {
                    fan_img.setImageResource(R.drawable.fan_on);
                } else {
                    fan_img.setImageResource(R.drawable.fan_off);
                }


                String light_status = snapshot.child(light.id).getValue().toString();
                boolean switch_light_state = light_status.equals("1");
                switch_light.setChecked(switch_light_state);
                if (switch_light_state) {
                    light_img.setImageResource(R.drawable.light_on);
                } else {
                    light_img.setImageResource(R.drawable.light_off);
                }

                String air_status = snapshot.child(air.id).getValue().toString();
                boolean switch_air_state = air_status.equals("1");
                switch_air.setChecked(switch_air_state);
                if (switch_air_state) {
                    air_img.setImageResource(R.drawable.air_conditioner_on);
                } else {
                    air_img.setImageResource(R.drawable.air_conditioner_off);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        mqttService = new MQTTService(this);
        switch_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean switch_fan_state = ((Switch) v).isChecked();

                if (switch_fan_state){
                    fan.data = "1";
                    fan_img.setImageResource(R.drawable.fan_on);
                }
                else{
                    fan.data = "0";
                    fan_img.setImageResource(R.drawable.fan_off);
                }
                mqttService.sendDataMQTT("light", fan);
                mData.child("Device").child(fan.id).setValue(fan.data);
            }
        });
        switch_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean switch_light_state = ((Switch) v).isChecked();

                if (switch_light_state){
                    light.data = "1";
                    light_img.setImageResource(R.drawable.light_on);
                }
                else{
                    light.data = "0";
                    light_img.setImageResource(R.drawable.light_off);
                }
                mqttService.sendDataMQTT("home", light);
                mData.child("Device").child(light.id).setValue(light.data);
            }
        });
        switch_air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean switch_air_state = ((Switch) v).isChecked();

                if (switch_air_state){
                    air.data = "1";
                    air_img.setImageResource(R.drawable.air_conditioner_on);
                }
                else{
                    air.data = "0";
                    air_img.setImageResource(R.drawable.air_conditioner_off);
                }
                mqttService.sendDataMQTT("home", air);
                mData.child("Device").child(air.id).setValue(air.data);
            }
        });

        mqttService.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
            }
            @Override
            public void connectionLost( Throwable cause){
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                Log.w(topic, message.toString());
                // String data_to_microbit = message.toString();
                // port.write(data_to_microbit.getBytes(),1000);
                Gson g = new Gson();
                Device device = g.fromJson(message.toString(), Device.class);
                String device_id = device.id;

                if (device.id.equals(tem_hum_sensor.id)){
                    String new_temp = device.data.split("-")[0];
                    String new_hum = device.data.split("-")[1];
                    temp.setText(new_temp + "\u00B0" + "C");
                    hum.setText(new_hum + "%");

                    mData.child("Device").child("7").setValue(device.data);
                }

                else if (device_id.equals(fan.id)){
                    boolean switch_fan_state = device.data.equals("1");
                    switch_fan.setChecked(switch_fan_state);
                    if (switch_fan_state) {
                        fan_img.setImageResource(R.drawable.fan_on);
                    } else {
                        fan_img.setImageResource(R.drawable.fan_off);
                    }
                    mData.child("Device").child(fan.id).setValue(device.data);
                }

                else if (device_id.equals(light.id)){
                    boolean switch_light_state = device.data.equals("1");
                    switch_light.setChecked(switch_light_state);
                    if (switch_light_state) {
                        light_img.setImageResource(R.drawable.light_on);
                    } else {
                        light_img.setImageResource(R.drawable.light_off);
                    }
                    mData.child("Device").child(light.id).setValue(device.data);
                }

                else if (device_id.equals(air.id)){
                    boolean switch_air_state = device.data.equals("1");
                    switch_air.setChecked(switch_air_state);
                    if (switch_air_state) {
                        air_img.setImageResource(R.drawable.air_conditioner_on);
                    } else {
                        air_img.setImageResource(R.drawable.air_conditioner_off);
                    }
                    mData.child("Device").child(air.id).setValue(device.data);
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
    }
}
