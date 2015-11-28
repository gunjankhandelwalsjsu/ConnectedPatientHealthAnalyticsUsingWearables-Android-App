package org.project.healthMeter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.project.healthMeter.R;
import org.project.healthMeter.presenter.PreScannerPresenter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by rajeshkhandelwal on 11/14/15.
 */
public class PreScannerAdapter  extends RecyclerView.Adapter<PreScannerAdapter.ViewHolder> {
    private Context mContext;
    private PreScannerPresenter presenter;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PreScannerAdapter(Context context, PreScannerPresenter presenter) {
        this.mContext = context;
        this.presenter = presenter;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PreScannerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_prescanner_item, parent, false);

        //loadDatabase();

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView ProductNameTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_ProductName);
       // TextView ExistingAllergyTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_ExistingAllergy);
        TextView AllergyResultTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_AllergyResult);
      //  TextView ExistingDiseaseTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_ExistingDisease);
        TextView sugarsConsumedTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_sugarsConsumed);
        TextView datetimeTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_time);

        TextView ProductNameTextViewTitle = (TextView) holder.mView.findViewById(R.id.item_preScanner_ProductName_title);
     //   TextView ExistingAllergyTextViewTitle = (TextView) holder.mView.findViewById(R.id.item_preScanner_ExistingAllergy_title);
        TextView AllergyResultTextViewTitle = (TextView) holder.mView.findViewById(R.id.item_preScanner_AllergyResult_title);
     //   TextView ExistingDiseaseTextViewTitle = (TextView) holder.mView.findViewById(R.id.item_preScanner_ExistingDisease_title);
        TextView sugarsConsumedTextViewTitle = (TextView) holder.mView.findViewById(R.id.item_preScanner_sugarsConsumed_title);
        Collections.addAll(presenter.getDatetime());

        TextView idTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_id);
        TextView typeTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_type);


        // Reverse ListView order to display latest items first
        Collections.addAll(presenter.getReading());
        Collections.addAll(presenter.getId());




        idTextView.setText(presenter.getId().get(position).toString());
        ProductNameTextView.setText(presenter.getReading().get(position).getFoodName().toString());
      //  ExistingAllergyTextView.setText(presenter.getReading().get(position).getExistingAllergy().toString());
        AllergyResultTextView.setText(presenter.getReading().get(position).getAllergyResult().toString());
     //   ExistingDiseaseTextView.setText(presenter.getReading().get(position).getExistingDisease().toString());
        sugarsConsumedTextView.setText(presenter.getReading().get(position).getSugarsConsumed().toString());
        ArrayList<String> time=presenter.getDatetime();
        Log.d("positionString", String.valueOf(position));
        String x=time.get(position);
        String text=presenter.convertDate(x);
        datetimeTextView.setText(text);
//        datetimeTextView.setText(presenter.convertDate(presenter.getDatetime().get(position)));
        //   typeTextView.setText(typeToString(presenter.getType().get(position)));
    }


/*
    private void loadDatabase(){
        // Get database from MainActivity
        presenter.loadDatabase();
    }*/

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return presenter.getReading().size();
    }
}