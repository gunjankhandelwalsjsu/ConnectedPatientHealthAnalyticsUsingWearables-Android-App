package org.glucosio.android.presenter;

import org.glucosio.android.db.DatabaseHandler;
import org.glucosio.android.db.GlucoseReading;
import org.glucosio.android.fragment.HistoryFragment;
import org.glucosio.android.tools.ReadingTools;
import java.util.ArrayList;

public class HistoryPresenter {

    DatabaseHandler dB;
    private ArrayList<Integer> id;
    private ArrayList<Double> reading;
    private ArrayList <Integer> type;
    private ArrayList<String> datetime;
    HistoryFragment fragment;

    public HistoryPresenter(HistoryFragment historyFragment) {
        this.fragment = historyFragment;
        dB = new DatabaseHandler(historyFragment.getActivity());
    }

    public boolean isdbEmpty(){
        return dB.getTemperatureReadings().size() == 0;
    }

    public void loadDatabase(){
        this.id = dB.getTemperatureIdAsArray();
        this.reading = dB.getTemperatureReadingAsArray();
        this.datetime = dB.getTemperatureDateTimeAsArray();
    }


    public String convertDate(String date) {
        ReadingTools rTools = new ReadingTools();
        return rTools.convertDate(date);
    }


    public void onUndoClicked(){
        fragment.notifyAdapter();
    }



    // Getters
    public ArrayList<Integer> getId() {
        return id;
    }

    public ArrayList<Double> getReading() {
        return reading;
    }

    public ArrayList<Integer> getType() {
        return type;
    }

    public ArrayList<String> getDatetime() {
        return datetime;
    }
}