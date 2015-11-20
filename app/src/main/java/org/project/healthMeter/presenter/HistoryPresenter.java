package org.project.healthMeter.presenter;

import org.project.healthMeter.db.DatabaseNewHandler;
import org.project.healthMeter.db.TemperatureReading;
import org.project.healthMeter.fragment.HistoryFragment;
import org.project.healthMeter.tools.ReadingTools;

import java.util.ArrayList;

public class HistoryPresenter {

    DatabaseNewHandler dB;
    private ArrayList<Long> id;
    private ArrayList<Double> reading;
    private ArrayList <Integer> type;
    private ArrayList<String> datetime;
    HistoryFragment fragment;

    public HistoryPresenter(HistoryFragment historyFragment) {
        this.fragment = historyFragment;
        dB = new DatabaseNewHandler(historyFragment.getActivity());
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

    public void onDeleteClicked(int idToDelete){
        removeReadingFromDb(dB.getTemperatureReadingById(idToDelete));
        fragment.notifyAdapter();
        fragment.updateToolbarBehaviour();
    }

    private void removeReadingFromDb(TemperatureReading gReading) {
        dB.deleteTemperatureReadings(gReading);
        fragment.reloadFragmentAdapter();
        loadDatabase();
    }

    // Getters
    public ArrayList<Long> getId() {
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

    public int getReadingsNumber(){
        return reading.size();
    }

}