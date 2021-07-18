package com.example.bk_home_smarter;

import android.os.Bundle;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bk_home_smarter.src.models.Device;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
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
        TextView tem = findViewById(R.id.txt);
        mqttService = new MQTTService(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Device device = new Device("11", "RELAY", "1", "");
                sendDataMQTT(device);
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
                Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_SHORT).show();

                TextView text = findViewById(R.id.txt);
                text.setText(device.toString());
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
    }



    public void sendDataMQTT(Device device){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);

        String dataString = device.toString();

        byte[] b = dataString.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        Log.d("ABC","Publish:"+ msg);
        try {
            mqttService.mqttAndroidClient.publish("bkdadn202/feeds/home", msg);
        } catch ( MqttException e){
            Log.d("MQTT", "sendDataMQTT: can not send anything");
        }

        Toast.makeText(TestMqtt.this, device.toString(), Toast.LENGTH_LONG).show();
    }
}
