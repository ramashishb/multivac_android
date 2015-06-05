package multivac.com.multivac;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by ramashish.baranwal on 05/06/15.
 */
@Table(name = "device")
public class Device extends Model {
    @Column(name="name")
    public String name;

    public Device() {}

    public Device(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static List<Device> getAllDevices() {
        return new Select()
                .from(Device.class)
                .orderBy("name ASC")
                .execute();
    }

    public static Device fromName(String name) {
        List<Device> devices = new Select()
                .from(Device.class)
                .where("name=?", name)
                .execute();
        if (devices.size() == 1) {
            return devices.get(0);
        }
        return null;
    }
}
