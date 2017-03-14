package com.example.colin.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by colin on 7/19/2016.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.MyViewHolder>{
    private List<Recommendation> recommendationlist;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, drystatus;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            drystatus = (TextView) view.findViewById(R.id.drystatus);
        }
    }


    public TimelineAdapter(List<Recommendation> recommendationlist) {
        this.recommendationlist = recommendationlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Recommendation mrecommendation = recommendationlist.get(position);
        holder.date.setText(mrecommendation.getDate());
    }

    @Override
    public int getItemCount() {
        return recommendationlist.size();
    }
}
