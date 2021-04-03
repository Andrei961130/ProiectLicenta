package com.example.pulseoximeter2021.Records.RegularUserAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
import com.example.pulseoximeter2021.R;

import java.util.ArrayList;


public class RegularUserAdapter2 extends RecyclerView.Adapter<RegularUserAdapter2.ViewHolder> {

    private Context context;
    private ArrayList<Record> records;

    public RegularUserAdapter2(ArrayList<Record> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public RegularUserAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_records_regular_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegularUserAdapter2.ViewHolder holder, int position) {

        holder.tvName.setText(records.get(position).getFullName());
        holder.tvDate.setText(records.get(position).getDateAndTimeAsString());
        holder.tvBpm.setText(records.get(position).getAverageBpmValue().toString());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvDate, tvBpm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.fragment_records_regular_user_tv_name);
            tvDate = itemView.findViewById(R.id.fragment_records_regular_user_tv_date);
            tvBpm = itemView.findViewById(R.id.fragment_records_regular_user_tv_bpm);
        }
    }

}
