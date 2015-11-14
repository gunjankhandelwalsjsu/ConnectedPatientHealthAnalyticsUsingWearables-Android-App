package org.project.healthMeter.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.project.healthMeter.R;
import org.project.healthMeter.fragment.HistoryFragment;
import org.project.healthMeter.fragment.OverviewFragment;
import org.project.healthMeter.fragment.PreScannerFragment;
import org.project.healthMeter.fragment.ScannerFragment;
import org.project.healthMeter.FirstPageFragmentListener;

/**
 * Created by paolo on 13/08/15.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    Context mContext;
    private final FragmentManager mFragmentManager;
    private Fragment mFragmentAtPos0;
    String email;

    public HomePagerAdapter(FragmentManager fm, Context context, String email) {
        super(fm);
        mFragmentManager = fm;

        this.email=email;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OverviewFragment();
            case 1:
                return new HistoryFragment();
            default:
                if (mFragmentAtPos0 == null) {
                    mFragmentAtPos0 = PreScannerFragment.newInstance(new FirstPageFragmentListener() {
                        public void onSwitchToNextFragment() {

                            mFragmentManager.beginTransaction().remove(mFragmentAtPos0).commit();
                            mFragmentAtPos0 = ScannerFragment.newInstance();
                            notifyDataSetChanged();
                        }
                    });
                }
                Bundle bundle = new Bundle();
                bundle.putString("email", email );
                PreScannerFragment frag = new PreScannerFragment();
                frag.setArguments(bundle);
                return frag;
        }
    }



    @Override
    public int getCount() {
        return 3;
    }

    // Workaround to refresh views with notifyDataSetChanged()
    public int getItemPosition(Object object) {
        if (object instanceof PreScannerFragment && mFragmentAtPos0 instanceof ScannerFragment)
            return POSITION_NONE;
        return POSITION_UNCHANGED;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.tab_overview);
            case 1:
                return mContext.getString(R.string.tab_history);
            default:
                return mContext.getString(R.string.tab_scan);
        }
    }




}

