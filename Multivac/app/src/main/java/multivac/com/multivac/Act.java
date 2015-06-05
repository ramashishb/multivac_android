package multivac.com.multivac;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

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

    public static List<Act> getAllActs() {
        return new Select()
                .from(Act.class)
                .orderBy("title ASC")
                .execute();
    }
}
