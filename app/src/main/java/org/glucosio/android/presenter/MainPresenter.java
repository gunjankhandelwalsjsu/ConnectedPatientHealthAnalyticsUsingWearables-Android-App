package org.glucosio.android.presenter;

import android.util.Log;

import org.glucosio.android.activity.MainActivity;
import org.glucosio.android.db.DatabaseNewHandler;
import org.glucosio.android.db.GlucoseReading;
import org.glucosio.android.db.TemperatureReading;
import org.glucosio.android.db.User;
import org.glucosio.android.tools.ReadingTools;
import org.glucosio.android.tools.SplitDateTime;
import org.glucosio.android.tools.TemperatureTools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainPresenter {

    MainActivity mainActivity;

    DatabaseNewHandler dB;
    User user;
    TemperatureTools rTools;

    private String readingYear;
    private String readingMonth;
    private String readingDay;
    private String readingHour;
    private String readingMinute;

    public MainPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        dB = new DatabaseNewHandler(mainActivity);
        rTools = new TemperatureTools();


    }


    public boolean isdbEmpty(){
        return dB.getTemperatureReadings().size() == 0;
    }


    public int hourToSpinnerType(int hour){
        return rTools.hourToSpinnerType(hour);
    }



    public void addValueTodb(String time,String reading){
        if(validateTime(time) && validateReading(reading)) {
            double finalReading = Double.parseDouble(reading);
       /*     getCurrentTime();
            String finalDateTime = readingYear + "-" + readingMonth + "-" + readingDay + " " + readingHour + ":" + readingMinute;
            Log.d("time",reading);*/
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(readingYear),Integer.parseInt(readingMonth)-1,Integer.parseInt(readingDay),Integer.parseInt(readingHour),Integer.parseInt(readingMinute));
            Date finalDateTime = cal.getTime();
            if(finalReading>30) {
                TemperatureReading gReading = new TemperatureReading(finalReading,finalDateTime);
                dB.addTemperatureReading(gReading);
            }
          //  mainActivity.dismissAddDialog();
        } else {
            mainActivity.showErrorMessage();
        }
    }



    public void startGittyReporter(){

    }

    private boolean validateTime(String time){
        return !time.equals("");
    }
    private boolean validateDate(String date){
        return !date.equals("");
    }
    private boolean validateReading(String reading){
        return !reading.equals("");
    }

    // Getters and Setters
    public String getReadingYear() {
        return readingYear;
    }

    public String getReadingMonth() {
        return readingMonth;
    }

    public String getReadingDay() {
        return readingDay;
    }

    public String getReadingHour() {
        return readingHour;
    }

    public String getReadingMinute() {
        return readingMinute;
    }


    public void setReadingYear(String readingYear) {
        this.readingYear = readingYear;
    }

    public void setReadingMonth(String readingMonth) {
        this.readingMonth = readingMonth;
    }

    public void setReadingDay(String readingDay) {
        this.readingDay = readingDay;
    }

    public void setReadingHour(String readingHour) {
        this.readingHour = readingHour;
    }

    public void setReadingMinute(String readingMinute) {
        this.readingMinute = readingMinute;
    }
}