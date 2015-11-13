package org.project.healthMeter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.project.healthMeter.R;
import org.project.healthMeter.presenter.ScannerPresenter;
import org.project.healthMeter.FirstPageFragmentListener;
import org.project.healthMeter.db.DatabaseNewHandler;

public class PreScannerFragment extends Fragment {

    TextView tvResult;
    private static final int Simple_Scanner_Activity = 1;
    private static final int Scanner_Activity = 2;
    TextView etResponse;
    Button button;
    Button button1;
    DatabaseNewHandler dB;
    String email;
    ScannerPresenter presenter;




    public static PreScannerFragment newInstance(FirstPageFragmentListener listener) {
        PreScannerFragment f = new PreScannerFragment();

//        Log.d("email",f.gEmail);
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {


        View root = inflater.inflate(R.layout.fragment_pre_scanner, container, false);
        Bundle bundle = this.getArguments();
        email = bundle.getString("email");

        button = (Button) root.findViewById(R.id.button);
                    button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("email", email );
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                ScannerFragment f=ScannerFragment.newInstance();
                f.setArguments(bundle);
                trans.replace(R.id.first_fragment_root_id, f);
                trans.addToBackStack(null);
                trans.commit();
           /*     trans.replace(R.id.first_fragment_root_id, ScannerFragment.newInstance());
                trans.addToBackStack(null);
                trans.commit();*/

            }

        });


        return root;

       // return mScannerView;
    }


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items

                return super.onOptionsItemSelected(item);
        }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onPause() {
        super.onPause();

    }


}


