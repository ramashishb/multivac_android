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

    public static List<EventAct> getAllEventActs() {
        return new Select()
                .from(EventAct.class)
                .orderBy("event ASC")
                .execute();
    }
}
