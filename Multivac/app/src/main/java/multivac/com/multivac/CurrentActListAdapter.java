package multivac.com.multivac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by ramashish.baranwal on 05/06/15.
 */
public class CurrentActListAdapter extends ArrayAdapter<Act> {
    private LayoutInflater mInflater;

    public CurrentActListAdapter(Context context, List<Act> objects) {
        super(context, 0, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getCurrentItemView(mInflater, convertView);
    }
}
