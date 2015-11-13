package org.project.healthMeter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.project.healthMeter.R;

import java.util.ArrayList;

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder> {
    private ArrayList<String> tipsList;


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
    public CameraAdapter(ArrayList<String> tips) {
        this.tipsList = tips;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CameraAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tips_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView tipTextView = (TextView) holder.mView.findViewById(R.id.fragment_tips_textview);
        tipTextView.setText(tipsList.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 3;
    }
}
