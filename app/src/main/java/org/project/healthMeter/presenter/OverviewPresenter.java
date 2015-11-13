package org.project.healthMeter.presenter;

import org.project.healthMeter.fragment.OverviewFragment;
import org.project.healthMeter.tools.TemperatureTools;
import org.project.healthMeter.db.DatabaseNewHandler;
import org.project.healthMeter.tools.TipsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by paolo on 10/09/15.
 */
public class OverviewPresenter {

    DatabaseNewHandler dB;
    private ArrayList<Double> reading;
    private List<Integer> readingsWeek;
    private List<Integer> readingsMonth;
    private List<String> datetimeWeek;
    private List<String> datetimeMonth;
    private OverviewFragment fragment;
    private ArrayList<String> datetime;

    public OverviewPresenter(OverviewFragment overviewFragment) {
        dB = new DatabaseNewHandler(overviewFragment.getActivity());
    }

    public boolean isdbEmpty(){
        return dB.getTemperatureReadings().size() == 0;
    }

    public void loadDatabase(){
        this.reading = dB.getTemperatureReadingAsArray();
        this.datetime = dB.getTemperatureDateTimeAsArray();
        this.readingsMonth = dB.getAverageTemperatureReadingsByMonth();
        this.readingsWeek = dB.getAverageTemperatureReadingsByWeek();
        this.datetimeWeek = dB.getTemperatureDatetimesByWeek();
        this.datetimeMonth = dB.getTemperatureDatetimesByMonth();
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


    public ArrayList<String> getDatetime() {
        return datetime;
    }

    public List<Integer> getReadingsWeek() {
        return readingsWeek;
    }

    public List<Integer> getReadingsMonth() {
        return readingsMonth;
    }

    public List<String> getDatetimeWeek() {
        return datetimeWeek;
    }

    public List<String> getDatetimeMonth() {
        return datetimeMonth;
    }
}
