package org.project.healthMeter.tools;

import android.content.Context;


import org.project.healthMeter.R;

import java.util.ArrayList;
import java.util.Collections;

public class TipsManager {
    private Context mContext;
    private int userAge;

    public TipsManager(Context mContext, int userAge) {
        this.mContext = mContext;
        this.userAge=userAge;
    }


    public ArrayList<String> getTips(){
        ArrayList<String> finalTips = new ArrayList<>();
        String[] allTips = mContext.getResources().getStringArray(R.array.tips_all);
        String[] plus40Tips = mContext.getResources().getStringArray(R.array.tips_all_age_plus_40);

            Collections.addAll(finalTips, allTips);
            Collections.addAll(finalTips, plus40Tips);

        return finalTips;
    }
}
