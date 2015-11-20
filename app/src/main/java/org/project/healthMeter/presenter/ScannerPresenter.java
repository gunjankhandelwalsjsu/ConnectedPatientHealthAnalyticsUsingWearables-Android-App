package org.project.healthMeter.presenter;

import android.util.Log;

import org.project.healthMeter.activity.ViewForScannerActivity;
import org.project.healthMeter.db.DatabaseNewHandler;
import org.project.healthMeter.db.FoodReading;
import org.project.healthMeter.db.User;
import org.project.healthMeter.fragment.ScannerFragment;
import org.project.healthMeter.tools.SplitDateTime;

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
    private String readingYear;
    private String readingMonth;
    private String readingDay;
    private String readingHour;
    private String readingMinute;
    String ExistingAllergy;
    String foodSafe;
    String foodUnsafe;
    String sugar;

    public ScannerPresenter(ViewForScannerActivity scannertActivity) {
        dB = new DatabaseNewHandler(scannertActivity);
    }

    public ScannerPresenter(ScannerFragment scannerFragment) {
        dB = new DatabaseNewHandler(scannerFragment.getActivity());

    }

    public Boolean isdbEmpty() {
        return dB.getFoodReadings().size()==0;
    }

    public void loadDatabase() {


    }
    public void getCurrentTime(){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date formatted = Calendar.getInstance().getTime();

        SplitDateTime addSplitDateTime = new SplitDateTime(formatted, inputFormat);
        this.readingYear = addSplitDateTime.getYear();
        this.readingMonth = addSplitDateTime.getMonth();
        this.readingDay = addSplitDateTime.getDay();
        this.readingHour = addSplitDateTime.getHour();
        this.readingMinute = addSplitDateTime.getMinute();
    }

    public void addValueTodb(String foodName,String ExistingAllergy,String ExistingDisease,String AllergyResult,String sugarsConsumed){


        getCurrentTime();
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(readingYear),Integer.parseInt(readingMonth)-1,Integer.parseInt(readingDay),Integer.parseInt(readingHour),Integer.parseInt(readingMinute));
        Date finalDateTime = cal.getTime();
        Log.d("time", String.valueOf(finalDateTime));

        FoodReading gReading = new FoodReading(foodName,ExistingAllergy,ExistingDisease,AllergyResult,sugarsConsumed,finalDateTime);
        dB.addFoodReading(gReading);

        //  mainActivity.dismissAddDialog();

    }
}