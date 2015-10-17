package org.glucosio.android.tools;

import android.content.Context;

import org.glucosio.android.R;

import java.util.ArrayList;
import java.util.Collections;

public class TipsManager {
    private Context mContext;
    private int userAge;

    public TipsManager(Context mContext) {
        this.mContext = mContext;
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
