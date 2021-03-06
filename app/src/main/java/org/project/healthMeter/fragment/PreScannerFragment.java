package org.project.healthMeter.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.project.healthMeter.FirstPageFragmentListener;
import org.project.healthMeter.R;
import org.project.healthMeter.activity.GetProductName;
import org.project.healthMeter.activity.MainActivity;
import org.project.healthMeter.adapter.PreScannerAdapter;
import org.project.healthMeter.db.DatabaseNewHandler;
import org.project.healthMeter.db.FoodReading;
import org.project.healthMeter.listener.RecyclerItemClickListener;
import org.project.healthMeter.presenter.PreScannerPresenter;

public class PreScannerFragment extends Fragment implements View.OnClickListener {


    DatabaseNewHandler dB;
    PreScannerPresenter presenter;

    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    FoodReading readingToRestore;
    FloatingActionButton scanButton;
    private Boolean isToolbarScrolling = true;
    FloatingActionButton preScannerbutton,productButton;





    public static PreScannerFragment newInstance(FirstPageFragmentListener listener) {
        PreScannerFragment f = new PreScannerFragment();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {

        View mFragmentView;
        presenter = new PreScannerPresenter(this);

        presenter.loadDatabase();
        if (!presenter.isdbEmpty()) {
            mFragmentView = inflater.inflate(R.layout.fragment_pre_scanner, container, false);
            preScannerbutton = (FloatingActionButton) mFragmentView.findViewById(R.id.preScannerbutton);
            preScannerbutton.setOnClickListener(this);
            productButton = (FloatingActionButton) mFragmentView.findViewById(R.id.productButton);
            productButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getContext(), GetProductName.class);
                    startActivity(i);
                 /*   FragmentTransaction trans = getFragmentManager().beginTransaction();
                    GetProductFragment f = GetProductFragment.newInstance();
                    trans.replace(R.id.first_fragment_root_id, f);
                    trans.addToBackStack(null);
                    trans.commit();*/
                }


            });


            mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.fragment_preScanner_recycler_view);
            mAdapter = new PreScannerAdapter(super.getActivity().getApplicationContext(), presenter);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(false);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(super.getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    // Do nothing
                }

                @Override
                public void onItemLongClick(final View view, final int position) {
                    CharSequence colors[] = new CharSequence[]{getResources().getString(R.string.dialog_delete)};

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setItems(colors, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // DELETE
                            TextView idTextView = (TextView) view.findViewById(R.id.item_preScanner_id);
                            final String idToDelete = idTextView.getText().toString();
                            final CardView item = (CardView) view.findViewById(R.id.item_preScanner);
                            item.animate().alpha(0.0f).setDuration(2000);

                            Snackbar.make(((MainActivity) getActivity()).getFabView(), R.string.fragment_history_snackbar_text, Snackbar.LENGTH_SHORT).setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    switch (event) {
                                        case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                            // Do nothing, see Undo onClickListener
                                            break;
                                        case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                                            presenter.onDeleteClicked(idToDelete);
                                            break;
                                    }
                                }

                                @Override
                                public void onShown(Snackbar snackbar) {
                                    // Do nothing
                                }
                            }).setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    presenter.onUndoClicked();
                                }
                            }).setActionTextColor(getResources().getColor(R.color.glucosio_accent)).show();
                        }

                    });
                    builder.show();
                }
            }));
            mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    mRecyclerView.removeOnLayoutChangeListener(this);
                    updateToolbarBehaviour();
                }
            });

        } else {
            mFragmentView = inflater.inflate(R.layout.fragment_prescanner_empty, container, false);

            scanButton = (FloatingActionButton) mFragmentView.findViewById(R.id.preScanbutton);
            scanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    FragmentTransaction trans =  getActivity().getSupportFragmentManager().beginTransaction();
                    ScannerFragment f=ScannerFragment.newInstance();
                    trans.replace(R.id.first_fragment_root_id, f);
                    trans.addToBackStack(null);
                    trans.commit();
                }

            });
            productButton = (FloatingActionButton) mFragmentView.findViewById(R.id.productButton);
            productButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getContext(), GetProductName.class);
                    startActivity(i);

                }


            });



        }




        return mFragmentView;
    }


        public void clickTakePhoto(View view){
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        ScannerFragment f=ScannerFragment.newInstance();
        trans.replace(R.id.first_fragment_root_id, f);
        trans.addToBackStack(null);
        trans.commit();

        }



    public void notifyAdapter(){
        mAdapter.notifyDataSetChanged();
    }

        public void reloadFragmentAdapter(){
            ((MainActivity)getActivity()).reloadFragmentAdapter();
            ((MainActivity)getActivity()).checkIfEmptyLayout();
        }




    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items

                return super.onOptionsItemSelected(item);
        }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onClick(View v) {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        ScannerFragment f=ScannerFragment.newInstance();
        trans.replace(R.id.first_fragment_root_id, f);
        trans.addToBackStack(null);
        trans.commit();
    }

    public void updateToolbarBehaviour(){
        if (mLayoutManager.findLastCompletelyVisibleItemPosition() == presenter.getReadingsNumber()-1) {
            isToolbarScrolling = false;
            ((MainActivity) getActivity()).turnOffToolbarScrolling();
        } else {
            if (!isToolbarScrolling){
                isToolbarScrolling = true;
                ((MainActivity)getActivity()).turnOnToolbarScrolling();
            }
        }
    }
}


