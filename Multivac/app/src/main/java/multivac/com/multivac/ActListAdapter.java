package multivac.com.multivac;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by ramashish.baranwal on 05/06/15.
 */
public class ActListAdapter extends ArrayAdapter<Act> {
    private LayoutInflater mInflater;

    public ActListAdapter(Context context, List<Act> objects) {
        super(context, 0, objects);
        mInflater = LayoutInflater.from(context);
    }
}
