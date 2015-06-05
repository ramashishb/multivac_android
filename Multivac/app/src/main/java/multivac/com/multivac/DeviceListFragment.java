package multivac.com.multivac;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

/**
 */
public class DeviceListFragment extends Fragment {
    public DeviceListFragment() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_list, container, false);
        final ListView deviceListView = (ListView) rootView.findViewById(R.id.devicelist);
        final List<Device> devices = Device.getAllDevices();
        final ArrayAdapter<Device> arrayAdapter = new ArrayAdapter<Device>(getActivity(),
                android.R.layout.simple_list_item_1, devices);
        deviceListView.setAdapter(arrayAdapter);

        View view = rootView.findViewById(R.id.device_add);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                final View dialogView = layoutInflater.inflate(R.layout.dialog_add_device, null);
                final AlertDialog.Builder dialog = builder.setView(dialogView);

                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = ((EditText) dialogView.findViewById(R.id.name)).getText().toString();
                        if (name == null || name.equals("")) {
                            Toast.makeText(getActivity(), "Missing data", Toast.LENGTH_SHORT);
                            dialogInterface.dismiss();
                            return;
                        }

                        if (Device.fromName(name) != null) {
                            Toast.makeText(getActivity(), "Already present", Toast.LENGTH_SHORT);
                            dialogInterface.dismiss();
                            return;
                        }

                        Device device = new Device();
                        device.save();
                        devices.clear();
                        devices.addAll(Device.getAllDevices());
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Added device", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Dismiss
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return rootView;
    }
}
