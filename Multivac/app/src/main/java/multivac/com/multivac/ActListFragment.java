package multivac.com.multivac;

import android.app.Activity;
import android.app.Dialog;
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

import com.activeandroid.query.Delete;

import java.util.List;

/**
 * Created by ramashish.baranwal on 05/06/15.
 */
public class ActListFragment extends Fragment {
    public ActListFragment() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_act_list, container, false);
        new Delete().from(Act.class).where("title=?", "").execute();
        final ListView actlistView = (ListView) rootView.findViewById(R.id.actlist);
        final List<Act> actions = Act.getActs();
        final ArrayAdapter<Act> arrayAdapter = new ArrayAdapter<Act>(getActivity(), android.R.layout.simple_list_item_1,
                actions);
        //ActListAdapter adapter = new ActListAdapter(getActivity(), Act.getActs());
        actlistView.setAdapter(arrayAdapter);

        View view = rootView.findViewById(R.id.act_add);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                final View dialogView = layoutInflater.inflate(R.layout.dialog_add_act, null);
                final AlertDialog.Builder dialog = builder.setView(dialogView);
                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO add action
                        String title = ((EditText) dialogView.findViewById(R.id.title)).getText().toString();
                        String action = ((EditText) dialogView.findViewById(R.id.action)).getText().toString();
                        String name = ((EditText) dialogView.findViewById(R.id.name)).getText().toString();
                        String data = ((EditText) dialogView.findViewById(R.id.data)).getText().toString();

                        if (title == null || title.equals("")
                                || action == null || action.equals("")
                                || name == null || name.equals("")
                                || data == null || data.equals("")) {
                            Toast.makeText(getActivity(), "Missing data", Toast.LENGTH_SHORT)
                                    .show();
                            dialogInterface.dismiss();
                            return;
                        }

                        Act act = new Act(title, action, name, data);
                        act.save();
                        actions.clear();
                        actions.addAll(Act.getActs());
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Added action", Toast.LENGTH_SHORT)
                            .show();
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
