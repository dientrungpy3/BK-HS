package com.example.bk_home_smarter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class HomeActivity extends AppCompatActivity {

    MQTTService mqttService;

    ImageView livingroom;
    ImageView bathroom;
    ImageView diningroom;
    ImageView bedroom;
    ImageView kitchen;
    ImageView playroom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        getViews();

        livingroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LivingRoomActivity.class);
                startActivity(intent);
            }
        });

        diningroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DiningRoomActivity.class);
                startActivity(intent);
            }
        });

        bedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BedRoomActivity.class);
                startActivity(intent);
            }
        });

        bathroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BathRoomActivity.class);
                startActivity(intent);
            }
        });


        playroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PlayRoomActivity.class);
                startActivity(intent);
            }
        });

        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, KitchenActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getViews(){
        livingroom = findViewById(R.id.livingroom);
        bathroom = findViewById(R.id.bathroom);
        diningroom = findViewById(R.id.diningroom);
        bedroom = findViewById(R.id.bedroom);
        kitchen = findViewById(R.id.kitchen);
        playroom = findViewById(R.id.playroom);
    }
}