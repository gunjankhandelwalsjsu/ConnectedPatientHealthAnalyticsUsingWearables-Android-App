package org.glucosio.android.presenter;

import android.util.Log;

import org.glucosio.android.activity.ScannerActivity;
import org.glucosio.android.activity.SensorTagActivity;
import org.glucosio.android.db.DatabaseNewHandler;
import org.glucosio.android.db.TemperatureReading;
import org.glucosio.android.db.User;
import org.glucosio.android.fragment.OverviewFragment;
import org.glucosio.android.fragment.PreScannerFragment;
import org.glucosio.android.fragment.ScannerFragment;
import org.glucosio.android.tools.SplitDateTime;
import org.glucosio.android.tools.TemperatureTools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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