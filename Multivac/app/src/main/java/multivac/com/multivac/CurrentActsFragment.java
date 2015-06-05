package multivac.com.multivac;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

/**
 * Created by ramashish.baranwal on 05/06/15.
 */
public class CurrentActsFragment extends Fragment {
    public CurrentActsFragment() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_acts_list, container, false);
        final ListView curActListView = (ListView) rootView.findViewById(R.id.current_act_list);
        final List<Act> currentActs = Act.getCurrentActs(getActivity());
        final CurrentActListAdapter actListAdapter = new CurrentActListAdapter(getActivity(), currentActs);
        curActListView.setAdapter(actListAdapter);
        return rootView;
    }
}
