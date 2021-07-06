package uart.terminal.androidstudio.com.myapplication;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;


public class MainActivity extends AppCompatActivity {

    MQTTService mqttService;
    TextView txtOut;
    EditText edtMessage;
    Button btnPublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();

        // connect & subcribe
        startMqtt();

        addEvents();

    }

    private void addEvents() {
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttService.publishMessage(edtMessage.getText().toString());
            }
        });
    }

    private void addControls() {
        txtOut = findViewById(R.id.txtOut);
        edtMessage = findViewById(R.id.edtMessage);
        btnPublish = findViewById(R.id.btnPublish);
    }


    private void startMqtt() {
        mqttService = new MQTTService(this);
        mqttService.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
//                dataReceived.setText(mqttMessage.toString());
                Log.d("BBB", mqttMessage.toString());
                txtOut.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

}
