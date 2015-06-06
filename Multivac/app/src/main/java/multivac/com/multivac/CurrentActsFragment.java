package multivac.com.multivac;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private ProgressDialog progressDialog = null;


    private static CurrentActsFragment sInstance;

    public static synchronized CurrentActsFragment getInstance() {
        if (sInstance == null) {
            sInstance = new CurrentActsFragment();
        }
        return sInstance;
    }

    public CurrentActsFragment() {}

    public void update(DeviceEvent de) {
        if (de != null) {
            DeviceEvent.addDeviceEvent(de);
        }

        if (progressDialog != null) {
            progressDialog.show();
        }
        UpdateActTask task = new UpdateActTask();
        task.execute();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_acts_list, container, false);
        final ListView curActListView = (ListView) rootView.findViewById(R.id.current_act_list);
        mCurrentActListAdapter = new CurrentActListAdapter(getActivity(), mCurrentActs);
        curActListView.setAdapter(mCurrentActListAdapter);
        update(null);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        return rootView;
    }

    private class UpdateActTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            Integer count = 0;
            List<Act> currentActs = Act.getCurrentActs(getActivity());
            for (Act act: currentActs) {
                if (act.updateAct(getActivity())) {
                    count += 1;
                }
            }
            mCurrentActs.clear();
            mCurrentActs.addAll(currentActs);
            Log.d("CurrentActsFragment", "Updated "+count+ " actions");
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (mCurrentActListAdapter != null) {
                mCurrentActListAdapter.notifyDataSetChanged();
            }
            if (progressDialog != null) {
                progressDialog.hide();
            }
        }
    }
}
