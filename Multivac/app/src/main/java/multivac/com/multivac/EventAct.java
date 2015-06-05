package multivac.com.multivac;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

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
}
