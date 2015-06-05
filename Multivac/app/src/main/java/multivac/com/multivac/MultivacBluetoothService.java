package multivac.com.multivac;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import multivac.com.multivac.util.BluetoothUtil;
import multivac.com.multivac.util.HelperUtil;

/**
 * Created by manish.patwari on 6/5/15.
 */
public class MultivacBluetoothService extends Service {

    private static  Thread workerThread;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
private static Boolean pollingStarted = false;

    public class LocalBinder extends Binder {
        MultivacBluetoothService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MultivacBluetoothService.this;
        }
    }

//    public MultivacBluetoothService() {
//        super(MultivacBluetoothService.class.getName());
//    }
//
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        // Gets data from the incoming Intent
//        if(!BluetoothUtil.isInitialized())
//        {
//            BluetoothUtil.initalize(getApplicationContext());
//        }
//        String dataString = intent.getDataString();
//        if(dataString.equals("start"))
//        {
//            startPollingForDevice();
//        }
//        else
//        {
//            stopPollingForDevice();
//        }
//    }


    public void startPollingForDevice(){
        if(!pollingStarted && !BluetoothUtil.isConnected() ) {
            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        BluetoothUtil.tryConnectingPairedDevices();
                        if(BluetoothUtil.isConnected())
                        {
                            HelperUtil.sendNotification(BluetoothUtil.getConnectedDevice().getName() + " Connected",getApplicationContext());
                            break;
                        }
                        try {
                            Thread.sleep(3000);

                        } catch (InterruptedException e) {

                        }
                    }

                }
            });
            pollingStarted = true;
            workerThread.start();
            HelperUtil.sendNotification(" Disconnected", getApplicationContext());
        }
    };

    public void stopPollingForDevice(){
        if(workerThread != null) {
            workerThread.interrupt();
            workerThread = null;
        }
        pollingStarted = false;

    }


    @Override
    public void onCreate() {
        if(!BluetoothUtil.isInitialized())
        {
            BluetoothUtil.initialize(getApplicationContext());

        }
        BluetoothUtil.setServiceInstance(this);
        startPollingForDevice();
        Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();
        return Service.START_STICKY;
    };
    @Override
    public void onDestroy() {
        Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        stopPollingForDevice();
    }
        @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
