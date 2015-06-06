package multivac.com.multivac;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by shashwat on 06/06/15.
 */
public class MapUtils {
    private final String URL = "http://maps.googleapis.com/maps/api/directions/json?";

    Context mContext;

    public static class DirectionResult {
        public final long distanceMeters;
        public final long durationSeconds;

        public DirectionResult(long distanceMeters, long durationSeconds) {
            this.distanceMeters = distanceMeters;
            this.durationSeconds = durationSeconds;
        }
    }

    public MapUtils(Context context) {
        mContext = context;
    }

    public DirectionResult getDirections(Location start, Location end) throws JSONException {
        String uri = URL
                + "origin=" + start.getLatitude() + "," + start.getLongitude()
                + "&destination=" + end.getLatitude() + "," + end.getLongitude();

        JSONObject jObject = getJSONFromURL(uri);

        JSONArray routesArray = jObject.getJSONArray("routes");

        if (routesArray.length() > 0) {
            JSONArray legsArray = routesArray.getJSONObject(0).getJSONArray("legs");
            if (legsArray.length() > 0) {
                JSONObject firstLeg = legsArray.getJSONObject(0);
                long distanceInMeters = firstLeg.getJSONObject("distance").getLong("value");
                long durationSeconds = firstLeg.getJSONObject("duration").getLong("value");

                return new DirectionResult(distanceInMeters, durationSeconds);
            }
        }
        return null;
    }

    public Location getLocation(String address) {
        if (mContext == null) {
            Log.e("MapUtils", "Null context!!");
            return null;
        }

        Geocoder geocoder = new Geocoder(mContext);
        if (geocoder == null) {
            Log.e("MapUtils", "Null geocoder");
            return null;
        }
        List addressList = null;
        try {
            addressList = geocoder.getFromLocationName(address, 1);
        }
        catch (IOException e) {
            Log.e("MapUtils", "Couldn't get location information for " + address);
        }
        if (addressList != null && addressList.size() > 0) {
            Address addressResult = (Address) addressList.get(0);
            Location locationResult = new Location("dummyProvider");
            locationResult.setLatitude(addressResult.getLatitude());
            locationResult.setLongitude(addressResult.getLongitude());
            return locationResult;
        }
        else {
            Log.e("MapUtils", "Couldn't get location information for " + address);
        }
        return null;
    }

    public DirectionResult getDirections(String start, String end) {
        String[] startTokens = start.split(",");
        String[] endTokens = end.split(",");
        Location startLocation = null, endLocation = null;
        if (startTokens.length == 2 && endTokens.length == 2) {
            try {
                startLocation = new Location("dummyProvider");
                startLocation.setLatitude(Float.parseFloat(startTokens[0]));
                startLocation.setLongitude(Float.parseFloat(startTokens[1]));

                endLocation = new Location("dummyProvider");
                endLocation.setLatitude(Float.parseFloat(endTokens[0]));
                endLocation.setLongitude(Float.parseFloat(endTokens[1]));
            }
            catch (Exception e) {
                Log.e("MapUtils", "Error parsing Latlong " + e);
            }
        }
        else {
            startLocation = getLocation(start);
            endLocation = getLocation(end);
        }

        if (startLocation == null || endLocation == null) {
            Log.e("MapUtils", "Couldn't get location information from "+start+" to "+end);
            return null;
        }

        DirectionResult result = null;
        try {
            result = getDirections(startLocation, endLocation);
        }
        catch (JSONException e) {
            Log.e("MapUtils", "Couldn't get directions from "+start+" to "+end);
        }
        return result;
    }

    public JSONObject getJSONFromURL(String url) throws JSONException {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream is = httpEntity.getContent();

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                return new JSONObject(sb.toString());
            } finally {
                is.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
