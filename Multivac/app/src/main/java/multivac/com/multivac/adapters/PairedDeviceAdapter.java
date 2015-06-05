package multivac.com.multivac.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import multivac.com.multivac.R;
import multivac.com.multivac.models.PairedDevice;

/**
 * Created by manish.patwari on 6/5/15.
 */
public class PairedDeviceAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    ArrayList<BluetoothDevice> bluetoothDevice;

    public PairedDeviceAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;

        bluetoothDevice = new ArrayList<BluetoothDevice>();
    }

    @Override
    public int getCount() {

        return bluetoothDevice.size();
    }

    @Override
    public Object getItem(int position) {
        return bluetoothDevice.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.paired_device_item, null);

//            if (isAdType(position)) {
//                convertView = new AdView(mContext, convertView, position);
//            }

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_name);


            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }


        // Get the current book's data in JSON form
        PairedDevice pairedDevice = (PairedDevice) getItem(position);


        // Send these Strings to the TextViews for display
        holder.name.setText(pairedDevice.getName());


        return convertView;
    }

    public void updateData(BluetoothDevice device) {
        // update the adapter's dataset
       this.bluetoothDevice.add(device);
        notifyDataSetChanged();
    }
    // this is used so you only ever have to do
    // inflation and finding by ID once ever per View
    private static class ViewHolder {
        public TextView name;

    }
}
