package org.glucosio.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.glucosio.android.FirstPageFragmentListener;
import org.glucosio.android.R;
import org.glucosio.android.fragment.AntherFragment;
import org.glucosio.android.fragment.HistoryFragment;
import org.glucosio.android.fragment.OverviewFragment;
import org.glucosio.android.fragment.PreScannerFragment;
import org.glucosio.android.fragment.ScannerFragment;

/**
 * Created by paolo on 13/08/15.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    Context mContext;
    private final FragmentManager mFragmentManager;
    private Fragment mFragmentAtPos0;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mFragmentManager = fm;

        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
              // return new OverviewFragment();
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
                return new PreScannerFragment();
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

