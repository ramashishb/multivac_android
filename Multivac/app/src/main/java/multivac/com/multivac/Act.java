package multivac.com.multivac;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import multivac.com.multivac.config.Config;

/**
 * Created by ramashish.baranwal on 05/06/15.
 */
@Table(name="act")
public class Act extends Model {
    public static final String TAG = "Act";

    @Column(name="title")
    public String title;

    @Column(name="action")
    public String action;

    @Column(name="name")
    public String name;

    @Column(name="data")
    public String data;

    @Column(name="description")
    public String description;

    public Act() {}

    public Act(String title, String action, String name, String data, String description) {
        this.title = title;
        this.action = action;
        this.name = name;
        this.data = data;
        this.description = description;
    }

    public String toString() {
        return title;
    }

    public View getCurrentItemView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.item_current_act, null);
            // TODO initialization, what?
        }
        else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.title);
        text.setText(title);

        TextView descriptionText = (TextView) view.findViewById(R.id.description);
        descriptionText.setText(description);
        return view;
    }

    private String getLocationInfo(String name) {
        String address = null;
        if ("Home".equalsIgnoreCase(name)) {
            address = Config.getInstance().getHomeAddress();
        }
        else if("Office".equalsIgnoreCase(name)) {
            address = Config.getInstance().getOfficeAddress();
        }
        if (address != null) {
            return address;
        }
        return name;
    }

    public boolean updateAct(Context context) {
        if ("Show".equals(action) && "Route".equals(name)) {
            String[] srcDest = data.split(",");
            Log.d("Act", "Getting route from "+srcDest[0]+" to "+srcDest[1]);
            String src = getLocationInfo(srcDest[0]), dest = getLocationInfo(srcDest[1]);

            MapUtils mapUtils = new MapUtils(context);
            MapUtils.DirectionResult directionResult = mapUtils.getDirections(src, dest);
            if (directionResult != null) {
                description = "To " + srcDest[1]
                        + "\nDistance: " + ((float) directionResult.distanceMeters) / 1000
                        + "\nTime to reach: " + directionResult.durationSeconds / 60 + " minutes";
                save();
                return true;
            }
        }
        return false;
    }

    public static List<Act> getAllActs(boolean dummy) {
        return new Select()
                .from(Act.class)
                .orderBy("title ASC")
                .execute();
    }

    private static void addActs() {
        new Act("Route to Home", "Show", "Route", "Office,Home", "30 minutes to Home").save();
        new Act("Route to Office", "Show", "Route", "Home,Office", "50 minutes to Office").save();
        new Act("Calendar", "Show", "Calendar", "Meeting", "10:00 AM - Goto market plan").save();
        new Act("Fill Petrol", "Fill", "Petrol", "", "Low fuel! Get filled").save();
    }

    public static List<Act> getAllActs() {
        List<Act> allActs = getAllActs(true);
        if (allActs.size() == 0) {
            addActs();
            allActs = getAllActs(true);
        }
        return allActs;
    }

    public static List<Act> getCurrentActs(Context context) {
        MobileEvent mobileEvent = MobileEvent.currentEvent(context);
        List<DeviceEvent> deviceEvents = DeviceEvent.currentEvents();

        Log.d(TAG, "Current mobile event: " + mobileEvent);
        Log.d(TAG, "Current device events: " + deviceEvents);
        From from = new Select().from(Event.class)
                .where("time=? and day=? and location=?", mobileEvent.getTime(),
                        mobileEvent.getDay(), mobileEvent.getLocation());
        List<Event> allEvents = new ArrayList<>();
        if (deviceEvents.size() > 0) {
            Log.d(TAG, "Applying device event filter");
            for (DeviceEvent de: deviceEvents) {
                List<Event> events = new Select().from(Event.class)
                        .where("device=? and state=? and time=? and day=? and location=?",
                                de.getDevice(), de.getState(), mobileEvent.getTime(),
                                mobileEvent.getDay(), mobileEvent.getLocation())
                        .execute();
                allEvents.addAll(events);
            }
        }
        else {
            Log.d(TAG, "Skipping device event filter for lack of data");
            List<Event> events = new Select().from(Event.class)
                    .where("time=? and day=? and location=?", mobileEvent.getTime(),
                            mobileEvent.getDay(), mobileEvent.getLocation())
                    .execute();
            allEvents.addAll(events);
        }
        Log.d(TAG, "Current events: " + allEvents);

        List<Act> currentActs = new ArrayList<>();
        Set<Long> actIds = new TreeSet<>();
        for (Event event: allEvents) {
            List<Act> acts = event.getActs();
            for (Act act: acts) {
                if (actIds.contains(act.getId())) {
                    continue;
                }
                currentActs.add(act);
            }
        }

        if (currentActs.isEmpty()) {
            Act noAct = new Act("Nothing", "", "", "", "Nothing to show here!");
            currentActs.add(noAct);
        }

        Log.d(TAG, "Current acts: " + currentActs);
        return currentActs;
    }
}
