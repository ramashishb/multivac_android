package multivac.com.multivac.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import multivac.com.multivac.MultivacBluetoothService;

/**
 * Created by manish.patwari on 6/5/15.
 */
public class BluetoothUtil {

  private static BluetoothAdapter mBluetoothAdapter;
  private static   BluetoothSocket mmSocket;
  private static   BluetoothDevice mmDevice;
  private static   OutputStream mmOutputStream;
  private static   InputStream mmInputStream;
  private static   Thread workerThread;
  private static   byte[] readBuffer;
  private static   int readBufferPosition;
  private static   volatile boolean stopWorker;
    private static Set<BluetoothDevice> pairedDevices;
    private BluetoothUtil(){};
    private static Context mApplicationContext;
    private static Context mContext;

    private  static int connected = 0;
    private static int bluetoothSupported;
    private static int initialized = 0;
    private static MultivacBluetoothService service;


    public static Boolean initialize(Context context)
    {
       // mContext = context;
        mApplicationContext = context.getApplicationContext();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null)
        {
           // myLabel.setText("No bluetooth adapter available");
            bluetoothSupported = 0;
            return false;
        }
        bluetoothSupported = 1;

      //  enableBluetooth(context);

        pairedDevices = mBluetoothAdapter.getBondedDevices();

        initialized = 1;
       return true;
    };

    public static void setServiceInstance(MultivacBluetoothService serviceInstance)
    {
        service = serviceInstance;
    }

    public static void enableBluetooth(Context context)
    {
        mContext = context;
        if(isBluetoothSupported() && !mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) context).startActivityForResult(enableBluetooth, 0);
        }
       // sendMessageToService("start");
    }
//    private static void sendMessageToService(String data){
//        Intent intent = new Intent(mApplicationContext, MultivacBluetoothService.class);
//        intent.setData(Uri.parse(data));
//        mApplicationContext.startService(intent);
//    }
    public static Set<BluetoothDevice> getPairedDevice(){
        if(pairedDevices.size() > 0)
        {
            return pairedDevices;
        }
        return null;
    }

    public static void connectToDevice(BluetoothDevice device)
    {
        mmDevice = device;
        if(mmDevice != null) {
            openBT();
        }
    };

    public static void connectToDevice(String deviceName){

        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals(deviceName))
                {
                    mmDevice = device;
                    if(mmDevice != null) {
                        openBT();
                    }
                    break;
                }
            }
        }
    };

    public static void tryConnectingPairedDevices(){
        connected = 0;
        if (!isBluetoothSupported()) {
            Log.e("BluetoothUtil", "Bluetooth not supported on this device");
            return;
        }
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if(!isConnected()) {
                    mmDevice = device;
                    if(mmDevice != null) {
                        openBT();
                    }
                }
                else
                {
                    break;
                }
            }
        }
    }

    private static void registerDeviceStatusListeners(){
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        mApplicationContext.registerReceiver(mReceiver, filter1);
        mApplicationContext.registerReceiver(mReceiver, filter2);
        mApplicationContext.registerReceiver(mReceiver, filter3);
    }

    public static int openBT()
    {
        //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID


        boolean temp = mmDevice.fetchUuidsWithSdp();
        UUID uuid = null;
        if( temp ){
            uuid = mmDevice.getUuids()[0].getUuid();
        }

        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            connected();
            registerDeviceStatusListeners();
        }
        catch (IOException ex)
        {
            //Toast.makeText(mApplicationContext.getApplicationContext(),"Device Can't be connected - " +ex.getMessage() , Toast.LENGTH_LONG).show();
            return 0;
        }
        beginListenForData();

        return 1;
        //myLabel.setText("Bluetooth Opened");
    }

    public static void beginListenForData()
    {
        final byte delimiter = '\n'; //This is the ASCII code for a newline character
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    stream.reset();
                    try
                    {
//                        int bytesAvailable = mmInputStream.available();

                        do {
                            int value = mmInputStream.read();
                            if (value > 0) {
                                if ((char)value == '\n') {
                                    break;
                                }
                                stream.write((byte)value);
                            } else {
                                break;
                            }
                        } while (true);

                        if(stream.size() >0)
                        {
                            broadCastData(new String(stream.toByteArray()));
                        }

//                         if(bytesAvailable > 0)
//                        {
//                            byte[] packetBytes = new byte[bytesAvailable];
//                            mmInputStream.read(packetBytes);
//                            for(int i=0;i<bytesAvailable;i++)
//                            {
//                                byte b = packetBytes[i];
//                                if(b == delimiter) {
//                                    byte[] encodedBytes = new byte[readBufferPosition];
//                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
//                                    final String data = new String(encodedBytes, "US-ASCII");
//                                    readBufferPosition = 0;
//                                    broadCastData(data);
//                                }
//                                else
//                                {
//                                    readBuffer[readBufferPosition++] = b;
//                                }
//                                Log.i("manish", " received data from device" + b);
//                            }
//                        }
//                        Log.i("manish", " received data" +bytesAvailable);

                       // Log.i("manish", new String(stream.toByteArray(), "US-ASCII"));

                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    public static void sendData(String data) throws IOException
    {
        //String msg = myTextbox.getText().toString();
        // msg += "\n";
        if(isConnected()) {
            mmOutputStream.write(data.getBytes());
        }
       // myLabel.setText("Data Sent");
    };

   public static String closeBT() throws IOException
    {
        try {
            stopWorker = true;
            try {
                if(mReceiver != null ) {
                    mApplicationContext.unregisterReceiver(mReceiver);
                }
            }
            catch (IllegalArgumentException e)
            {

            }

            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            return  "Bluetooth Closed";
        }
        catch (IOException e)
        {

        }
        return  null;
    }



    private static final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
               // connected();
               // sendMessageToService("stop");
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {

            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                disconnected();
            }
        }
    };

    private static void connected(){
        broadCastConnected();
        connected = 1;
        service.stopPollingForDevice();
    }

    private static void disconnected(){

        broadCastDisconnected();
        connected = 0;
        mmDevice = null;
        service.startPollingForDevice();
        pairedDevices = null;
       // sendMessageToService("start");
    }

    static public Boolean isConnected(){
        return (connected == 1);
    }

    static public Boolean isBluetoothSupported(){
        return (bluetoothSupported == 1);
    }


    static public Boolean isInitialized(){
        return (initialized == 1);
    }

    static public BluetoothDevice getConnectedDevice(){
        return mmDevice;
    }

    private static void broadCastData(String data){
        Intent intent = new Intent();
        intent.setAction("multivacEvents");
        intent.putExtra("data", data);
        mApplicationContext.sendBroadcast(intent);
    }
    private static void broadCastConnected(){
        Intent intent = new Intent();
        intent.setAction("multivacEvents");
        intent.putExtra("data", 1);
        mApplicationContext.sendBroadcast(intent);
    }
    private static void broadCastDisconnected(){
        Intent intent = new Intent();
        intent.setAction("multivacEvents");
        intent.putExtra("data", 0);
        mApplicationContext.sendBroadcast(intent);
    }
}
