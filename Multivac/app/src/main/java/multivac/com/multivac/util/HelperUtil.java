package multivac.com.multivac.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import multivac.com.multivac.MainActivity;
import multivac.com.multivac.R;

/**
 * Created by manish.patwari on 6/6/15.
 */
public class HelperUtil {

    public static void sendNotification(String message,Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.mipmap.ic_launcher;
        CharSequence notiText = "Your notification from the service";
        long meow = System.currentTimeMillis();

        Notification notification = new Notification(icon, notiText, meow);

        CharSequence contentTitle = message;
        CharSequence contentText = "";
        //CharSequence contentText = "Device Address  " + BluetoothUtil.getConnectedDevice().getAddress() ;
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

        int SERVER_DATA_RECEIVED = 1;
        notificationManager.notify(SERVER_DATA_RECEIVED, notification);
    }
}
