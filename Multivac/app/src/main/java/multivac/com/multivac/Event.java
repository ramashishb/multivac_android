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

    @Column(name="code")
    public int code;

    @Column(name="state")
    public String state;

    @Column(name="time")
    public String time;

    @Column(name="day")
    public String day;

    @Column(name="location")
    public String location;

    public List<Act> getActs() {
        return new Select()
                .from(Act.class)
                .innerJoin(EventAct.class)
                .on("act.id = eventact.act")
                .where("eventact.act = ?", getId())
                .execute();
    }
}
