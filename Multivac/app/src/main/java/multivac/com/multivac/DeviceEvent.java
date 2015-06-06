package multivac.com.multivac;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramashish.baranwal on 06/06/15.
 */
public class DeviceEvent {
    private String device, state;

    public DeviceEvent() {}
    private static List<DeviceEvent> events = new ArrayList<>();
    public DeviceEvent(String device, String state) {
        this.device = device;
        this.state = state;
    }

    public String toString() {
        return device + "-" + state;
    }

    public String getDevice() {
        return device;
    }

    public String getState() {
        return state;
    }


    public static void addDeviceEvent(DeviceEvent event){
        events.clear();
        events.add(event);
    }

    public static List<DeviceEvent> currentEvents() {

        // TODO
        return events;
    }
}
