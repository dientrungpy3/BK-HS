package com.example.bk_home_smarter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bk_home_smarter.src.models.Device;
import com.example.bk_home_smarter.src.utils.DatabaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LivingRoomActivity extends AppCompatActivity {

    MQTTService mqttService;
    ListView lvDevice;
    ArrayList<Device> listDevice;
    DeviceAdapter adapter;
    DatabaseUtils databaseUtils;
    DatabaseReference mData;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_room);

        adapter = new DeviceAdapter(this, R.layout.activity_living_room, listDevice);
        listDevice = new ArrayList<Device>();
        listDevice.add(new Device("deviceId", "roomId", "deviceName", true, "light"));
        lvDevice.setAdapter(adapter);

//        mData = FirebaseDatabase.getInstance().getReference();
//        mData.child("Device").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//
////                txt.setText(snapshot.getValue().toString());
//
//                Device device = snapshot.getValue(Device.class);
//                if ((device != null) && device.roomId.equals("livingroom") ){
//                    listDevice.add(device);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
    }

}