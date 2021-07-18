package com.example.bk_home_smarter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class TestFirebaseActivity extends AppCompatActivity {

    DatabaseReference mData;
    TextView txtTestGetRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_firebase);

        txtTestGetRoom = (TextView) findViewById(R.id.txtTestDB);

        mData = FirebaseDatabase.getInstance().getReference();

        mData.child("Device").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                txtTestGetRoom.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}