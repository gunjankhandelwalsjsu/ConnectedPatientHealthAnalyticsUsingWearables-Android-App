package org.project.healthMeter.tools;

import android.content.Context;


import org.project.healthMeter.R;

import java.util.ArrayList;
import java.util.Collections;

public class TipsManager {
    private Context mContext;
    private int userAge;

    public TipsManager(Context mContext, String birthDate) {
        this.mContext = mContext;

        int bYear=Integer.parseInt(birthDate.substring(0,3));
        int age=2015-bYear;
        this.userAge = age;
    }


    public ArrayList<String> getTips(){
        ArrayList<String> finalTips = new ArrayList<>();
        String[] allTips = mContext.getResources().getStringArray(R.array.tips_all);
        String[] plus40Tips = mContext.getResources().getStringArray(R.array.tips_all_age_plus_40);
        if (userAge >= 40){
            Collections.addAll(finalTips, allTips);
            Collections.addAll(finalTips, plus40Tips);
        } else {
            Collections.addAll(finalTips, allTips);
        }
        return finalTips;
    }
}
