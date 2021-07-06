package com.example.democlient;

import android.os.Bundle;

import com.example.myfirstapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;


public class MainActivity extends AppCompatActivity {

    MQTTService mqttService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with no one action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mqttService = new MQTTService(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendDataMQTT(String data){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);

        byte[] b = data.getBytes(Charset.forName("UTF−8"));
        msg.setPayload(b);

        Log.d("ABC","Publish:"+ msg);
        try {
                mqttService.mqttAndroidClient.publish("bkdadn202/feeds/light", msg);
        } catch ( MqttException e){
            Log.d("MQTT", "sendDataMQTT: can not send anything");
        }
    }
}