package com.example.bk_home_smarter.src.utils;


import com.example.bk_home_smarter.src.models.Device;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DatabaseUtils {

    public ArrayList<Device> getDeviceByRoom(String roomId){
        ArrayList<Device> listDevice = new ArrayList<Device>();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

        mData.child("Device").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot snapshot, String previousChildName) {
                Device device = snapshot.getValue(Device.class);
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        return listDevice;
    }
}
