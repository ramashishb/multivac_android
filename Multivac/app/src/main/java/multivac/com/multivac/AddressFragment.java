package multivac.com.multivac;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import multivac.com.multivac.config.Config;

/**
 * Created by manish.patwari on 6/6/15.
 */
public class AddressFragment extends Fragment {
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_address, container, false);

        final EditText homeAddress = (EditText)rootView.findViewById(R.id.home_address);
        final EditText officeAddress = (EditText)rootView.findViewById(R.id.office_address);
        final Button addAddress = (Button)rootView.findViewById(R.id.add_address);

        homeAddress.setText(Config.getInstance().getHomeAddress());
        officeAddress.setText(Config.getInstance().getOfficeAddress());

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String hAddress = homeAddress.getText().toString();
                String oAddress = officeAddress.getText().toString();
                if(hAddress.trim().equals("") && oAddress.trim().equals("") )
                {
                    Toast.makeText(getActivity(),"Enter valid address",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Config.getInstance().saveHomeAddress(hAddress);
                    Config.getInstance().saveOfficeAddress(oAddress);
                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}
