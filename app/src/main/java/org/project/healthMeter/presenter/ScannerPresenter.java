package org.project.healthMeter.presenter;

import org.project.healthMeter.fragment.ScannerFragment;
import org.project.healthMeter.db.DatabaseNewHandler;
import org.project.healthMeter.db.User;

/**
 * Created by rajeshkhandelwal on 11/10/15.
 */
public class ScannerPresenter {


    DatabaseNewHandler dB;
    User user;

    public ScannerPresenter(ScannerFragment scannerFragment) {
        dB = new DatabaseNewHandler(scannerFragment.getActivity());
    }

    public void isdbEmpty(String email) {
        //return dB.getUser(email).equals(null);
    }

    public void loadDatabase(String email) {
     //   this.user = dB.getUser(email);


    }
}