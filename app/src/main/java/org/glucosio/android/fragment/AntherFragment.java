package org.glucosio.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.glucosio.android.FirstPageFragmentListener;
import org.glucosio.android.R;
import org.glucosio.android.adapter.CameraAdapter;
import org.glucosio.android.adapter.HomePagerAdapter;
import org.glucosio.android.db.DatabaseHandler;
import org.glucosio.android.tools.TipsManager;

public class AntherFragment extends Fragment {

    private DatabaseHandler dB;
    private TipsManager tipsManager;
    private RecyclerView tipsRecycler;
    private CameraAdapter adapter;

    public static AntherFragment newInstance() {
        AntherFragment fragment = new AntherFragment();

        return fragment;
    }

    public AntherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // dB = ((MainActivity)getActivity()).getDatabase();
        // tipsManager = new TipsManager(getActivity(), dB.getUser(1).get_age());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_camera, container, false);
        // tipsRecycler = (RecyclerView) mView.findViewById(R.id.fragment_tips_recyclerview);
        // adapter = new TipsAdapter(tipsManager.getTips());

        // LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        // llm.setOrientation(LinearLayoutManager.VERTICAL);
        // tipsRecycler.setLayoutManager(llm);
        // tipsRecycler.setAdapter(adapter);
        // tipsRecycler.setHasFixedSize(false);
        return mView;
    }


}