package org.glucosio.android.presenter;

import android.util.Log;

import org.glucosio.android.db.DatabaseNewHandler;
import org.glucosio.android.db.TemperatureReading;
import org.glucosio.android.fragment.HistoryFragment;
import org.glucosio.android.fragment.OverviewFragment;
import org.glucosio.android.tools.ReadingTools;
import org.glucosio.android.tools.TemperatureTools;
import org.glucosio.android.tools.TipsManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by paolo on 10/09/15.
 */
public class OverviewPresenter {

    DatabaseNewHandler dB;
    private ArrayList<Double> reading;
    private ArrayList <Integer> type;
    private ArrayList<String> datetime;

    public OverviewPresenter(OverviewFragment overviewFragment) {
        dB = new DatabaseNewHandler(overviewFragment.getActivity());
    }

    public boolean isdbEmpty(){
        return dB.getTemperatureReadings().size() == 0;
    }

    public void loadDatabase(){
        this.reading = dB.getTemperatureReadingAsArray();
       // this.datetime = dB.getTemperatureDateTimeAsArray();
    }

    public String convertDate(String date) {
        TemperatureTools rTools = new TemperatureTools();
        return rTools.convertDate(date);
    }

   /* public int getGlucoseTrend(){
        return dB.getAverageTemperatureReadingForLastMonth();
    }
*/
    public String getLastReading(){
        return getReading().get(getReading().size() - 1) + "";
    }

    public String getRandomTip(TipsManager manager){
        ArrayList<String> tips = manager.getTips();

        // Get random tip from array
        int randomNumber = new Random().nextInt(tips.size());
        return tips.get(randomNumber);
    }


    public ArrayList<Double> getReading() {
        return reading;
    }

    public ArrayList<Integer> getType() {
        return type;
    }

    public ArrayList<String> getDatetime() {
        Log.i("datetimearray", datetime.get(0));
        return datetime;

    }
}
