package multivac.com.multivac;

import android.location.Location;

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

/**
 * Created by shashwat on 06/06/15.
 */
public class MapUtils {
    private final String URL = "http://maps.googleapis.com/maps/api/directions/json?";

    public static class DirectionResult {
        public final long distanceMeters;
        public final long durationSeconds;

        public DirectionResult(long distanceMeters, long durationSeconds) {
            this.distanceMeters = distanceMeters;
            this.durationSeconds = durationSeconds;
        }
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