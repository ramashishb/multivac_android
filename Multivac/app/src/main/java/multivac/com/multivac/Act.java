package multivac.com.multivac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by ramashish.baranwal on 05/06/15.
 */
@Table(name="act")
public class Act extends Model {
    @Column(name="title")
    public String title;

    @Column(name="action")
    public String action;

    @Column(name="name")
    public String name;

    @Column(name="data")
    public String data;

    public Act() {}

    public Act(String title, String action, String name, String data) {
        this.title = title;
        this.action = action;
        this.name = name;
        this.data = data;
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

        TextView description = (TextView) view.findViewById(R.id.description);
        description.setText(data);
        return view;
    }

    public static List<Act> getAllActs() {
        return new Select()
                .from(Act.class)
                .orderBy("title ASC")
                .execute();
    }

    public static List<Act> getCurrentActs(Context context) {
        MobileEvent mobileEvent = MobileEvent.currentEvent(context);
        List<Event> events = new Select().from(Event.class)
                .where("time=? and day=? and location=?", mobileEvent.getTime(),
                        mobileEvent.getDay(), mobileEvent.getLocation())
                .execute();
        List<Act> currentActs = new ArrayList<>();
        Set<Long> actIds = new TreeSet<>();
        for (Event event: events) {
            List<Act> acts = event.getActs();
            for (Act act: acts) {
                if (actIds.contains(act.getId())) {
                    continue;
                }
                currentActs.add(act);
            }
        }

        if (currentActs.isEmpty()) {
            Act noAct = new Act("Nothing", "", "", "Nothing to show here!");
            currentActs.add(noAct);
        }
        return currentActs;
    }
}
