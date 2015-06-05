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
import android.widget.Toast;

import java.util.List;

/**
 * Created by ramashish.baranwal on 05/06/15.
 */
public class EventListFragment extends Fragment {
    public EventListFragment() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        final ListView eventlistView = (ListView) rootView.findViewById(R.id.eventlist);
        final List<Event> events = Event.getAllEvents();
        final ArrayAdapter<Event> arrayAdapter = new ArrayAdapter<Event>(getActivity(),
                android.R.layout.simple_list_item_1, events);
        eventlistView.setAdapter(arrayAdapter);

        View view = rootView.findViewById(R.id.event_add);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                final View dialogView = layoutInflater.inflate(R.layout.dialog_add_event, null);
                final AlertDialog.Builder dialog = builder.setView(dialogView);

                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = ((EditText) dialogView.findViewById(R.id.title)).getText().toString();
                        String device = ((EditText) dialogView.findViewById(R.id.device)).getText().toString();
                        String state = ((EditText) dialogView.findViewById(R.id.state)).getText().toString();
                        String time = ((EditText) dialogView.findViewById(R.id.time)).getText().toString();
                        String day = ((EditText) dialogView.findViewById(R.id.day)).getText().toString();
                        String location = ((EditText) dialogView.findViewById(R.id.location)).getText().toString();

                        if (title == null || title.equals("")
                                || device == null || device.equals("")
                                || state == null || state.equals("")
                                || time == null || time.equals("")
                                || day == null || day.equals("")
                                || location == null || location.equals("")) {
                            Toast.makeText(getActivity(), "Missing data", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                            return;
                        }

                        Event event = new Event(title, device, state, time, day, location);
                        event.save();
                        events.clear();
                        events.addAll(Event.getAllEvents());
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Added event", Toast.LENGTH_SHORT).show();
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
