package com.example.bk_home_smarter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class PlayRoomActivity extends AppCompatActivity {

    MQTTService mqttService;
    MQTTService mqttServiceSub;

    DatabaseReference mData;

    Device tem_hum_sensor = new Device("7", "TEMP-HUMID", "0-0", "*C-%");
    Device fan = new Device("11", "RELAY", "0", "");
    Device light = new Device("31", "RELAY", "0", "");
    Device air = new Device("32", "RELAY", "0", "");


    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText hour, minute, second;
    private Button save, remove, cancel;


    //Declare timer
    CountDownTimer cTimer_light = null;
    CountDownTimer cTimer_fan = null;
    CountDownTimer cTimer_air = null;

    Switch switch_fan;
    Switch switch_light;
    Switch switch_air;


    boolean switch_fan_state = false;
    boolean switch_light_state = false;
    boolean switch_air_state = false;

    ImageView fan_img;
    ImageView air_img;
    ImageView light_img;


    TextView time_light, time_fan, time_air;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_room);



        time_light = (TextView) findViewById(R.id.time_light_play);
        time_fan = (TextView) findViewById(R.id.time_fan_play);
        time_air = (TextView) findViewById(R.id.time_air_play);

        // Create view
        TextView temp = findViewById(R.id.txt_play_temp);
        TextView hum = findViewById(R.id.txt_play_hum);
        switch_fan = findViewById(R.id.fan_play_switch);
        switch_light = findViewById(R.id.light_play_switch);
        switch_air = findViewById(R.id.air_play_switch);

        ImageView clock_fan = findViewById(R.id.clock_fan_play);
        ImageView clock_light = findViewById(R.id.clock_light_play);
        ImageView clock_air = findViewById(R.id.clock_air_play);

        clock_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog("fan");
            }
        });


        clock_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog("light");
            }
        });


        clock_air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog("air");
            }
        });

        fan_img = (ImageView) findViewById(R.id.fan_play);
        air_img = (ImageView) findViewById(R.id.air_play);
        light_img = (ImageView) findViewById(R.id.light_play);
        ImageView back = (ImageView) findViewById(R.id.play_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayRoomActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

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
                switch_fan_state = fan_status.equals("1");
                switch_fan.setChecked(switch_fan_state);
                if (switch_fan_state) {
                    fan_img.setImageResource(R.drawable.fan_on);
                } else {
                    fan_img.setImageResource(R.drawable.fan_off);
                }


                String light_status = snapshot.child(light.id).getValue().toString();
                 switch_light_state = light_status.equals("1");
                switch_light.setChecked(switch_light_state);
                if (switch_light_state) {
                    light_img.setImageResource(R.drawable.light_on);
                } else {
                    light_img.setImageResource(R.drawable.light_off);
                }

                String air_status = snapshot.child(air.id).getValue().toString();
                switch_air_state = air_status.equals("1");
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
        mqttServiceSub = new MQTTService(this, "CSE_BBC");

        switch_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 switch_fan_state = ((Switch) v).isChecked();

                if (switch_fan_state){
                    fan.data = "1";
                    fan_img.setImageResource(R.drawable.fan_on);
                }
                else{
                    fan.data = "0";
                    fan_img.setImageResource(R.drawable.fan_off);
                }
                mqttService.sendDataMQTT("bk-iot-relay", fan);
                mData.child("Device").child(fan.id).setValue(fan.data);
            }
        });
        switch_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 switch_light_state = ((Switch) v).isChecked();

                if (switch_light_state){
                    light.data = "1";
                    light_img.setImageResource(R.drawable.light_on);
                }
                else{
                    light.data = "0";
                    light_img.setImageResource(R.drawable.light_off);
                }
                mqttService.sendDataMQTT("bk-iot-relay", light);
                mData.child("Device").child(light.id).setValue(light.data);
            }
        });
        switch_air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 switch_air_state = ((Switch) v).isChecked();

                if (switch_air_state){
                    air.data = "1";
                    air_img.setImageResource(R.drawable.air_conditioner_on);
                }
                else{
                    air.data = "0";
                    air_img.setImageResource(R.drawable.air_conditioner_off);
                }
                mqttService.sendDataMQTT("bk-iot-relay", air);
                mData.child("Device").child(air.id).setValue(air.data);
            }
        });

        /*
        Set call back for CSE_BBC
        call back for CSE_BBC1 will also be included
         */
        mqttServiceSub.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
            }
            @Override
            public void connectionLost( Throwable cause){
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // Log.w(topic, message.toString());
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
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
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

//                if (device.id.equals(tem_hum_sensor.id)){
//                    String new_temp = device.data.split("-")[0];
//                    String new_hum = device.data.split("-")[1];
//                    temp.setText(new_temp + "\u00B0" + "C");
//                    hum.setText(new_hum + "%");
//
//                    mData.child("Device").child("7").setValue(device.data);
//                }

                if (device_id.equals(fan.id)){
                     switch_fan_state = device.data.equals("1");
                    switch_fan.setChecked(switch_fan_state);
                    if (switch_fan_state) {
                        fan_img.setImageResource(R.drawable.fan_on);
                    } else {
                        fan_img.setImageResource(R.drawable.fan_off);
                    }
                    mData.child("Device").child(fan.id).setValue(device.data);
                }

                else if (device_id.equals(light.id)){
                     switch_light_state = device.data.equals("1");
                    switch_light.setChecked(switch_light_state);
                    if (switch_light_state) {
                        light_img.setImageResource(R.drawable.light_on);
                    } else {
                        light_img.setImageResource(R.drawable.light_off);
                    }
                    mData.child("Device").child(light.id).setValue(device.data);
                }

                else if (device_id.equals(air.id)){
                     switch_air_state = device.data.equals("1");
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


    public void createDialog(String device){
        dialogBuilder = new AlertDialog.Builder(this);
        final View clockpopup = getLayoutInflater().inflate(R.layout.popup, null);
        hour = (EditText) clockpopup.findViewById(R.id.hour);
        minute = (EditText) clockpopup.findViewById(R.id.minute);
        second = (EditText) clockpopup.findViewById(R.id.second);

        save = (Button) clockpopup.findViewById(R.id.save);
        remove = (Button) clockpopup.findViewById(R.id.remove);
        cancel = (Button) clockpopup.findViewById(R.id.cancel);

        dialogBuilder.setView(clockpopup);
        dialog = dialogBuilder.create();
        dialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int int_hour = Integer.parseInt(hour.getText().toString());
                int int_minute = Integer.parseInt(minute.getText().toString());
                int int_second = Integer.parseInt(second.getText().toString());

                setCountDown(device, int_hour, int_minute, int_second);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (device.equals("fan")){
                    cTimer_fan.cancel();
                    time_fan.setText("");
                }
                else if (device.equals("light")){
                    cTimer_light.cancel();
                    time_light.setText("");
                }
                else if (device.equals("air")){
                    cTimer_air.cancel();
                    time_air.setText("");
                }
            }
        });

    }

    private void setCountDown(String device, int hour, int minute, int second) {
        int time_delta = (hour*3600 + minute*60 + second)*1000;
        if (device.equals("fan")){
            cTimer_fan= new CountDownTimer(time_delta, 1000) {
                public void onTick(long millisUntilFinished) {
                    int scound = (int) millisUntilFinished/1000;
                    String time_remain = String.format("%02d:%02d:%02d", scound / 3600, scound % 3600 / 60, scound % 60);
                    time_fan.setText(time_remain);
                }
                public void onFinish() {
                    switch_fan_state = (switch_fan_state == false) ? true : false;
                    switch_fan.setChecked(switch_fan_state);
                    if (switch_fan_state) {
                        fan_img.setImageResource(R.drawable.fan_on);
                        fan.data = "1";
                    } else {
                        fan_img.setImageResource(R.drawable.fan_off);
                        fan.data = "0";
                    }
                    mData.child("Device").child(fan.id).setValue(fan.data);
                    mqttService.sendDataMQTT("bk-iot-relay", fan);
                    time_fan.setText("");
                }
            };
            cTimer_fan.start();
        }
        else if  (device.equals("light")){
            cTimer_light = new CountDownTimer(time_delta, 1000) {
                public void onTick(long millisUntilFinished) {
                    int scound = (int) millisUntilFinished/1000;
                    String time_remain = String.format("%02d:%02d:%02d", scound / 3600, scound % 3600 / 60, scound % 60);
                    time_light.setText(time_remain);
                }
                public void onFinish() {
                    switch_light_state = (switch_light_state) ? false : true;
                    switch_light.setChecked(switch_light_state);
                    if (switch_light_state) {
                        light_img.setImageResource(R.drawable.light_on);
                        light.data = "1";
                    } else {
                        light_img.setImageResource(R.drawable.light_off);
                        light.data = "0";
                    }
                    mData.child("Device").child(light.id).setValue(light.data);
                    mqttService.sendDataMQTT("bk-iot-relay", light);
                    time_light.setText("");
                }
            };
            cTimer_light.start();
        }
        else if (device.equals("air")){
            cTimer_air = new CountDownTimer(time_delta, 1000) {
                public void onTick(long millisUntilFinished) {
                    int scound = (int) millisUntilFinished/1000;
                    String time_remain = String.format("%02d:%02d:%02d", scound / 3600, scound % 3600 / 60, scound % 60);
                    time_air.setText(time_remain);
                }
                public void onFinish() {
                    switch_air_state = (switch_air_state == false) ? true : false;
                    switch_air.setChecked(switch_air_state);
                    if (switch_air_state) {
                        air_img.setImageResource(R.drawable.air_conditioner_on);
                        air.data = "1";
                    } else {
                        air_img.setImageResource(R.drawable.air_conditioner_off);
                        air.data = "0";
                    }
                    mData.child("Device").child(air.id).setValue(air.data);
                    mqttService.sendDataMQTT("bk-iot-relay", air);
                    time_air.setText("");
                }
            };
            cTimer_air.start();
        }
    }

}
