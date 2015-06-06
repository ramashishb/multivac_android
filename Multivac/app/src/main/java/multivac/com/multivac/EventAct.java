package multivac.com.multivac;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by ramashish.baranwal on 05/06/15.
 */
@Table(name="eventact")
public class EventAct extends Model {
    @Column(name="event")
    public Event event;

    @Column(name="act")
    public Act act;

    public EventAct() {}

    public EventAct(Event event, Act act) {
        this.event = event;
        this.act = act;
    }

    public String toString() {
        return event.toString() + "->" + act.toString();
    }

    public static EventAct fromEventAct(Event event, Act act) {
        List<EventAct> eventActs = new Select()
                .from(EventAct.class)
                .where("event=? and act=?", event.getId(), act.getId())
                .execute();
        if (eventActs.size() > 0) {
            for (int i=1; i<eventActs.size(); ++i)
                eventActs.get(i).delete();
            return eventActs.get(0);
        }
        return null;
    }

    public static List<EventAct> getAllEventActs(boolean dummy) {
        return new Select()
                .from(EventAct.class)
                .orderBy("event ASC")
                .execute();
    }

    private static void addEventActs(List<Event> events, List<Act> acts) {
        for (Event e: events) {
            for (Act a: acts) {
                new EventAct(e, a).save();
            }
        }
    }

    private static void addEventActs() {
        List<Event> events = Event.getAllEvents();
        List<Act> acts = Act.getAllActs();

        events = new Select().from(Event.class)
                .where("title LIKE ?", "Leave Office for Home%").execute();
        acts = new Select().from(Act.class)
                .where("title = ?", "Route to Home").execute();
        addEventActs(events, acts);

        events = new Select().from(Event.class)
                .where("device = ? and state = ?", "car", "near").execute();
        acts = new Select().from(Act.class)
                .where("title = ?", "Fill Petrol").execute();
        addEventActs(events, acts);

        events = new Select().from(Event.class)
                .where("title LIKE ?", "Enter Office%").execute();
        acts = new Select().from(Act.class)
                .where("title = ?", "Calendar").execute();
        addEventActs(events, acts);

    }

    public static List<EventAct> getAllEventActs() {
        List<EventAct> allEventActs = getAllEventActs(true);
        if (allEventActs.size() == 0) {
            addEventActs();
            allEventActs = getAllEventActs(true);
        }
        return allEventActs;
    }
}
