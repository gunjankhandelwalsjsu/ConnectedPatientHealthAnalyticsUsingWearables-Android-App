package org.project.healthMeter.presenter;

import org.project.healthMeter.db.DatabaseNewHandler;
import org.project.healthMeter.db.FoodReading;
import org.project.healthMeter.db.User;
import org.project.healthMeter.fragment.PreScannerFragment;
import org.project.healthMeter.tools.SplitDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rajeshkhandelwal on 11/14/15.
 */
public class PreScannerPresenter {
    DatabaseNewHandler dB;
    User user;
    private String readingYear;
    private String readingMonth;
    private String readingDay;
    private String readingHour;
    private String readingMinute;
    private ArrayList<String> id;
    String AllergyResult;
    String ExistingDisease;
    String foodName;

    String sugarsConsumed;
    private ArrayList<FoodReading> reading=new ArrayList<>();
    String ExistingAllergy;
    PreScannerFragment fragment;


    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getAllergyResult() {
        return AllergyResult;
    }

    public void setAllergyResult(String allergyResult) {
        AllergyResult = allergyResult;
    }


    public String getExistingDisease() {
        return ExistingDisease;
    }

    public void setExistingDisease(String existingDisease) {
        ExistingDisease = existingDisease;
    }

    public String getSugarsConsumed() {
        return sugarsConsumed;
    }

    public void setSugarsConsumed(String sugarsConsumed) {
        this.sugarsConsumed = sugarsConsumed;
    }

    public PreScannerFragment getFragment() {
        return fragment;
    }

    public void setFragment(PreScannerFragment fragment) {
        this.fragment = fragment;
    }





    public ArrayList<String> getId() {
        return id;
    }

    public void setId(ArrayList<String> id) {
        this.id = id;
    }

    public ArrayList<FoodReading> getReading() {

        return reading;
    }

    public void setReading(ArrayList<FoodReading> reading)
    {
        this.reading = reading;
    }


    public String getExistingAllergy() {
        return ExistingAllergy;
    }

    public void setExistingAllergy(String existingAllergy) {
        ExistingAllergy = existingAllergy;
    }





    public PreScannerPresenter(PreScannerFragment preScannerFragment) {
        dB = new DatabaseNewHandler(preScannerFragment.getActivity());
    }

    public Boolean isdbEmpty()
    {
        return dB.getFoodReadings().size()==0;

    }

    public void loadDatabase() {
        this.id = dB.getFoodIdAsArray();
        for(int i=0;i<dB.getFoodReadingAsArrayOfObjects().size();i++)
        {
            FoodReading single=new FoodReading();
            single.setFoodName(dB.getFoodReadingAsArrayOfObjects().get(i).getFoodName());
            single.setSugarsConsumed(dB.getFoodReadingAsArrayOfObjects().get(i).getSugarsConsumed());
            single.setExistingDisease(dB.getFoodReadingAsArrayOfObjects().get(i).getExistingDisease());
            single.setExistingAllergy(dB.getFoodReadingAsArrayOfObjects().get(i).getExistingAllergy());
            single.setAllergyResult(dB.getFoodReadingAsArrayOfObjects().get(i).getAllergyResult());
            reading.add(single);

        }

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

   /* public void addValueTodb(String foodName,String ExistingAllergy,String ExistingDisease,String AllergyResult,String sugarsConsumed){


        getCurrentTime();
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(readingYear),Integer.parseInt(readingMonth)-1,Integer.parseInt(readingDay),Integer.parseInt(readingHour),Integer.parseInt(readingMinute));
        Date finalDateTime = cal.getTime();
        Log.d("time", String.valueOf(finalDateTime));

        FoodReading gReading = new FoodReading(finalDateTime,this.reading);
        dB.addFoodReading(gReading);

        //  mainActivity.dismissAddDialog();

    }
*/
    public void onUndoClicked() {
        fragment.notifyAdapter();
    }
}
