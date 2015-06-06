package multivac.com.multivac;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import multivac.com.multivac.util.BluetoothUtil;


public class MainActivity extends ActionBarActivity
{
    TextView status;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = CurrentActsFragment.getInstance();
        String title = "Whats Now?";
        fragmentManager.beginTransaction().replace(R.id.container, fragment)
                .commit();
        setTitle("Multivac - " + title);

//        Button openButton = (Button)findViewById(R.id.open);
//        Button sendButton = (Button)findViewById(R.id.send);
//        Button closeButton = (Button)findViewById(R.id.close);
//        deviceName = (EditText)findViewById(R.id.device_name);
//        myLabel = (TextView)findViewById(R.id.label);
//        myTextbox = (EditText)findViewById(R.id.entry);
        status = (TextView) findViewById(R.id.status);


        BluetoothUtil.enableBluetooth(this);
        IntentFilter filter = new IntentFilter("multivacEvents");
        registerReceiver(receiver,filter);
        //Open Button
//        openButton.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                try
//                {
//                    BluetoothUtil.connectToDevice(deviceName.getText().toString());
//                    myLabel.setText("Bluetooth Opened");
//                }
//                catch (Exception ex) {
//                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        //Send Button
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                try {
//                    BluetoothUtil.sendData(myTextbox.getText().toString());
//                    myLabel.setText("Data Sent");
//                } catch (IOException ex) {
//                }
//            }
//        });
//
//        //Close button
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                try {
//                    myLabel.setText(BluetoothUtil.closeBT());
//
//                } catch (IOException ex) {
//                }
//            }
//        });

        startService(new Intent(this, MultivacBluetoothService.class));
    }

    private android.content.BroadcastReceiver receiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            if (data == null)
            {
                return;
            }
            if("1".equals(data))
            {
                //Toast.makeText(getApplicationContext(), "Connect", Toast.LENGTH_LONG).show();
                status.setText("Connected");
            }
            else if("0".equals(data))
            {
                //Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_LONG).show();
                status.setText("Disconnected");
            }
            else
            {
                String[] dataArray = data.split(",");
                CurrentActsFragment.getInstance().update(new DeviceEvent(dataArray[0],dataArray[1]));
                status.setText(data);
            }
        }
    };


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

        String title = null;
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.action_current_acts) {
            fragment = CurrentActsFragment.getInstance();
            title = "Whats Now?";
        }
        if (id == R.id.action_acts) {
            fragment = new ActListFragment();
            title = "Actions";
        }
        else if (id == R.id.action_events) {
            fragment = new EventListFragment();
            title = "Events";
        }
        else if (id == R.id.action_eventacts) {
            fragment = new EventActListFragment();
            title = "Events:Actions";
        }
        else if (id == R.id.action_devices) {
            fragment = new DeviceListFragment();
            title = "Devices";
        }

        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            setTitle("Multivac - " + title);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
           // myLabel.setText(BluetoothUtil.closeBT());
            unregisterReceiver(receiver);

    }
}