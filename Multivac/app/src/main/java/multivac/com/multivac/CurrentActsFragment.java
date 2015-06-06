package multivac.com.multivac;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramashish.baranwal on 05/06/15.
 */
public class CurrentActsFragment extends Fragment {
    private static final String TAG = "CurrentActsFragment";

    private List<Act> mCurrentActs = new ArrayList<>();
    private CurrentActListAdapter mCurrentActListAdapter;

    private static CurrentActsFragment sInstance;

    public static synchronized CurrentActsFragment getInstance() {
        if (sInstance == null) {
            sInstance = new CurrentActsFragment();
        }
        return sInstance;
    }

    public CurrentActsFragment() {}

    public void update(DeviceEvent de) {
        DeviceEvent.addDeviceEvent(de);
        List<Act> currentActs = Act.getCurrentActs(getActivity());
        mCurrentActs.clear();
        mCurrentActs.addAll(currentActs);
        if (mCurrentActListAdapter != null) {
            mCurrentActListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_acts_list, container, false);
        final ListView curActListView = (ListView) rootView.findViewById(R.id.current_act_list);
        List<Act> currentActs = Act.getCurrentActs(getActivity());
        mCurrentActs.clear();
        mCurrentActs.addAll(currentActs);
        mCurrentActListAdapter = new CurrentActListAdapter(getActivity(), mCurrentActs);
        curActListView.setAdapter(mCurrentActListAdapter);
        return rootView;
    }
}
