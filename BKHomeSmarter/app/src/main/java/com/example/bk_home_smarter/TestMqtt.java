package com.example.bk_home_smarter;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;


public class TestMqtt extends AppCompatActivity {

    MQTTService mqttService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mqtt);

        Button fab = findViewById(R.id.button);
        mqttService = new MQTTService(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataMQTT("YES");
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
                // String data_to_microbit = message.toString();
                // port.write(data_to_microbit.getBytes(),1000);
                Log.d(topic, message.toString());
                Toast myToast = Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_SHORT);

                myToast.show();
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
    }



    public void sendDataMQTT(String data){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);

        byte[] b = data.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        Log.d("ABC","Publish:"+ msg);
        try {
            mqttService.mqttAndroidClient.publish("bkdadn202/feeds/home", msg);
        } catch ( MqttException e){
            Log.d("MQTT", "sendDataMQTT: can not send anything");
        }
    }
}