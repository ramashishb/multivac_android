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
 * Created by ramashish.baranwal on 05/06/15.
 */
public class EventActListFragment extends Fragment {
    public EventActListFragment() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_act_list, container, false);
        final ListView eventActListView = (ListView) rootView.findViewById(R.id.eventactlist);
        final List<EventAct> eventActs = EventAct.getAllEventActs();
        final ArrayAdapter<EventAct> arrayAdapter = new ArrayAdapter<EventAct>(getActivity(),
                android.R.layout.simple_list_item_1, eventActs);
        eventActListView.setAdapter(arrayAdapter);

        View view = rootView.findViewById(R.id.eventact_add);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                final View dialogView = layoutInflater.inflate(R.layout.dialog_add_event_act, null);
                final AlertDialog.Builder dialog = builder.setView(dialogView);
                List<Event> events = Event.getAllEvents();
                List<Act> acts = Act.getAllActs();
                final ArrayAdapter<Event> eventAdapter = new ArrayAdapter<Event>(getActivity(),
                        android.R.layout.simple_list_item_1, events);
                final ArrayAdapter<Act> actAdapter = new ArrayAdapter<Act>(getActivity(),
                        android.R.layout.simple_list_item_1, acts);

                final Spinner eventSpinner = (Spinner) dialogView.findViewById(R.id.event);
                eventSpinner.setAdapter(eventAdapter);
                final Spinner actSpinner = (Spinner) dialogView.findViewById(R.id.act);
                actSpinner.setAdapter(actAdapter);

                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Event event = (Event) eventSpinner.getSelectedItem();
                        Act act = (Act) actSpinner.getSelectedItem();
                        EventAct eventAct = new EventAct(event, act);
                        eventAct.save();
                        eventActs.clear();
                        eventActs.addAll(EventAct.getAllEventActs());
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Added event action mapping", Toast.LENGTH_SHORT).show();
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
