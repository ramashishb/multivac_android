package multivac.com.multivac;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by ramashish.baranwal on 05/06/15.
 */
@Table(name="event")
public class Event extends Model {
    @Column(name="title")
    public String title;

    @Column(name="device")
    public String device;

    @Column(name="state")
    public String state;

    @Column(name="time")
    public String time;

    @Column(name="day")
    public String day;

    @Column(name="location")
    public String location;

    public Event() {}

    public Event(String title, String device, String state, String time, String day, String location) {
        this.title = title;
        this.device = device;
        this.state = state;
        this.time = time;
        this.day = day;
        this.location = location;
    }

    public String toString() {
        return title;
    }

    public List<Act> getActs() {
        return new Select()
                .from(Act.class)
                .innerJoin(EventAct.class)
                .on("act.id = eventact.act")
                .where("eventact.event = ?", getId())
                .execute();
    }

    public static List<Event> getAllEvents(boolean dummy) {
        return new Select()
                .from(Event.class)
                .orderBy("title ASC")
                .execute();
    }

    private static void addEvents() {
        String[] weekdays = {"Friday", "Saturday"};
        String[] weekends = {"Saturday", "Sunday"};
        for (String day: weekdays) {
            new Event("Leave Home for Office at Morning: "+day, "car", "near", "Morning", day, "Home").save();
            new Event("Enter Office at Morning: "+day, "car", "away", "Morning", day, "Office").save();
            new Event("Enter Office at Noon: "+day, "car", "away", "Noon", day, "Office").save();
            new Event("Enter Office at Evening: "+day, "car", "away", "Evening", day, "Office").save();
            new Event("Leave Office for Home at Morning: "+day, "car", "near", "Morning", day, "Office").save();
            new Event("Leave Office for Home at Noon: "+day, "car", "near", "Noon", day, "Office").save();
            new Event("Leave Office for Home at Evening: "+day, "car", "near", "Evening", day, "Office").save();
            new Event("Enter Home at Evening: "+day, "car", "away", "Evening", day, "Home").save();
        }
    }

    public static List<Event> getAllEvents() {
        List<Event> allEvents = getAllEvents(true);
        if (allEvents.size() == 0) {
            addEvents();
            allEvents = getAllEvents(true);
        }
        return allEvents;
    }
}
