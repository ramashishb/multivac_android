package multivac.com.multivac;

import android.app.Activity;
import android.content.*;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import multivac.com.multivac.util.BluetoothUtil;


public class MainActivity extends Activity
{
    TextView myLabel;
    EditText myTextbox;
    EditText deviceName;
    TextView status;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openButton = (Button)findViewById(R.id.open);
        Button sendButton = (Button)findViewById(R.id.send);
        Button closeButton = (Button)findViewById(R.id.close);
        deviceName = (EditText)findViewById(R.id.device_name);
        myLabel = (TextView)findViewById(R.id.label);
        myTextbox = (EditText)findViewById(R.id.entry);
        status = (TextView) findViewById(R.id.status);


        BluetoothUtil.enableBluetooth(this);
        IntentFilter filter = new IntentFilter("multivacEvents");
        registerReceiver(receiver,filter);
        //Open Button
        openButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    BluetoothUtil.connectToDevice(deviceName.getText().toString());
                    myLabel.setText("Bluetooth Opened");
                }
                catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Send Button
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    BluetoothUtil.sendData(myTextbox.getText().toString());
                    myLabel.setText("Data Sent");
                } catch (IOException ex) {
                }
            }
        });

        //Close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    myLabel.setText(BluetoothUtil.closeBT());

                } catch (IOException ex) {
                }
            }
        });

        startService(new Intent(this, MultivacBluetoothService.class));
    }

    private android.content.BroadcastReceiver receiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getDataString();
            if(data == "1")
            {
                //Toast.makeText(getApplicationContext(), "Connect", Toast.LENGTH_LONG).show();
                status.setText("Connected");
            }
            else if(data == "0")
            {
                //Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_LONG).show();
                status.setText("Disconnected");
            }
            else
            {
                myLabel.setText(data);
            }
        }
    };





    @Override
    protected void onDestroy() {
        super.onDestroy();
           // myLabel.setText(BluetoothUtil.closeBT());
            unregisterReceiver(receiver);
    }
}