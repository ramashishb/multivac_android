package multivac.com.multivac;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by ramashish.baranwal on 05/06/15.
 */
public class MobileEvent {
    private String time, day, location;

    public MobileEvent() {}

    public MobileEvent(String time, String day, String location) {
        this.time = time;
        this.day = day;
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public String getDay() {
        return day;
    }

    public String getLocation() {
        return location;
    }

    public String toString() {
        return time + "-" + day + "-" + location;
    }

    /* Get events from Mobile at this moment. */
    public static MobileEvent currentEvent(Context context) {
        Date now = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
        String week_day = dateFormat.format(now);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String time;
        if (hour >= 4 && hour < 11) {
            time = "Morning";
        }
        else if (hour >= 11 && hour < 16) {
            time = "Noon";
        }
        else if (hour >= 16 && hour < 20) {
            time = "Evening";
        }
        else {
            time = "Night";
        }

        // Location
        String location = "Office";
        /*
        TODO
        GoogleApiClient mClient = new GoogleApiClient.Builder(context).build();
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            // TODO: get location
        }
        */

        return new MobileEvent(time, week_day, location);
    }
}
