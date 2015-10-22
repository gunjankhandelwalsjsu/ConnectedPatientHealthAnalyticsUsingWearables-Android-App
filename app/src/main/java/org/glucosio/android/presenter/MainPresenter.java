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

    public void getCurrentTime(){
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateobj = new Date();

        String formatted = inputFormat.format(dateobj);
        SplitDateTime addSplitDateTime = new SplitDateTime(formatted, inputFormat);

        this.readingYear = addSplitDateTime.getYear();
        this.readingMonth = addSplitDateTime.getMonth();
        this.readingDay = addSplitDateTime.getDay();
        this.readingHour = addSplitDateTime.getHour();
        this.readingMinute = addSplitDateTime.getMinute();
    }

    public int timeToSpinnerType() {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formatted = inputFormat.format(Calendar.getInstance().getTime());
        SplitDateTime addSplitDateTime = new SplitDateTime(formatted, inputFormat);
        int hour = Integer.parseInt(addSplitDateTime.getHour());

        if (hour > 4 && hour <= 7 ){
            return 0;
        } else if (hour > 7 && hour <= 11){
            return 1;
        } else if (hour > 11 && hour <= 13) {
            return 2;
        } else if (hour > 13 && hour <= 17) {
            return 3;
        } else if (hour > 17 && hour <= 20) {
            return 4;
        } else if (hour > 20 && hour <= 4) {
            return 5;
        } else {
            return 0;
        }
    }

    public int hourToSpinnerType(int hour){
        return rTools.hourToSpinnerType(hour);
    }



    public void addValueTodb(String time,String reading){
        if(validateTime(time) && validateReading(reading)) {
            double finalReading = Double.parseDouble(reading);
            getCurrentTime();
            String finalDateTime = readingYear + "-" + readingMonth + "-" + readingDay + " " + readingHour + ":" + readingMinute;
            Log.d("time",reading);
            if(finalReading>30) {
                TemperatureReading gReading = new TemperatureReading(finalReading);
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