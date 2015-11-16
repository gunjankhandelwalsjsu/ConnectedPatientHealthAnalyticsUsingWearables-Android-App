package org.project.healthMeter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.project.healthMeter.R;
import org.project.healthMeter.presenter.PreScannerPresenter;

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
        TextView ExistingAllergyTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_ExistingAllergy);
        TextView AllergyResultTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_AllergyResult);
        TextView ExistingDiseaseTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_ExistingDisease);
        TextView sugarsConsumedTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_sugarsConsumed);

        TextView ProductNameTextViewTitle = (TextView) holder.mView.findViewById(R.id.item_preScanner_ProductName_title);
        TextView ExistingAllergyTextViewTitle = (TextView) holder.mView.findViewById(R.id.item_preScanner_ExistingAllergy_title);
        TextView AllergyResultTextViewTitle = (TextView) holder.mView.findViewById(R.id.item_preScanner_AllergyResult_title);
        TextView ExistingDiseaseTextViewTitle = (TextView) holder.mView.findViewById(R.id.item_preScanner_ExistingDisease_title);
        TextView sugarsConsumedTextViewTitle = (TextView) holder.mView.findViewById(R.id.item_preScanner_sugarsConsumed_title);

        TextView idTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_id);
        TextView typeTextView = (TextView) holder.mView.findViewById(R.id.item_preScanner_type);


        // Reverse ListView order to display latest items first
        Collections.addAll(presenter.getReading());
        Collections.addAll(presenter.getId());


        // if (db.getUser(1).getUnitMeasurement == mmolL){
        //    readingTextView.setText(convert.toMmolL(reading.get(position)) + "mmol/l");
        //}

        idTextView.setText(presenter.getId().get(position).toString());
        ProductNameTextView.setText(presenter.getReading().get(position).getFoodName().toString());
        ExistingAllergyTextView.setText(presenter.getReading().get(position).getExistingAllergy().toString());
        AllergyResultTextView.setText(presenter.getReading().get(position).getAllergyResult().toString());
        ExistingDiseaseTextView.setText(presenter.getReading().get(position).getExistingDisease().toString());
        sugarsConsumedTextView.setText(presenter.getReading().get(position).getSugarsConsumed().toString());

//        datetimeTextView.setText(presenter.convertDate(presenter.getDatetime().get(position)));
        //   typeTextView.setText(typeToString(presenter.getType().get(position)));
    }

    public String typeToString(int typeInt){
        //TODO refactor this ugly mess
        String typeString = "";
        if (typeInt == 0) {
            typeString = mContext.getString(R.string.dialog_add_type_1);
        } else if (typeInt == 1) {
            typeString = mContext.getString(R.string.dialog_add_type_2);
        } else if (typeInt == 2) {
            typeString = mContext.getString(R.string.dialog_add_type_3);
        } else if (typeInt == 3) {
            typeString = mContext.getString(R.string.dialog_add_type_4);
        } else if (typeInt == 4) {
            typeString = mContext.getString(R.string.dialog_add_type_5);
        } else if (typeInt == 5) {
            typeString = mContext.getString(R.string.dialog_add_type_6);
        } else if (typeInt == 6) {
            typeString = mContext.getString(R.string.dialog_add_type_7);
        } else if (typeInt == 7) {
            typeString = mContext.getString(R.string.dialog_add_type_8);
        } else if (typeInt == 8) {
            typeString = mContext.getString(R.string.dialog_add_type_9);
        }
        return typeString;
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