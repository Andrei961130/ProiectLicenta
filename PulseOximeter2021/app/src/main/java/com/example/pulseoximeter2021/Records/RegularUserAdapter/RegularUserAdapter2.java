package com.example.pulseoximeter2021.Records.RegularUserAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
import com.example.pulseoximeter2021.MainScreen.MainActivity;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Records.RecordViewActivity;

import java.util.ArrayList;
import java.util.Locale;


public class RegularUserAdapter2 extends RecyclerView.Adapter<RegularUserAdapter2.ViewHolder> {

    private Context context;
    private ArrayList<Record> records;

    public RegularUserAdapter2(ArrayList<Record> records, Context context) {
        this.records = records;
        this.context = context;
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

        String duration = String.format(Locale.ENGLISH, "%d seconds", records.get(position).getLength());
        holder.tvDuration.setText(duration);
        holder.tvDate.setText(records.get(position).getDateAndTimeAsString());
        holder.tvBpm.setText(records.get(position).getAverageBpmValue().toString());

//        GradientDrawable gd = new GradientDrawable();
//        gd.setColor(Color.RED);
//        gd.setCornerRadius(20);
//        gd.setStroke(2, Color.WHITE);
//        holder.setBackgroundDrawable(gd);

        if(records.get(position).getAverageBpmValue() <= 70)
        {
            holder.tvBpm.setTextColor(Color.GREEN);
            holder.iv_heartbeat.setColorFilter(Color.GREEN);
        }

        if(records.get(position).getAverageBpmValue() > 70
        && records.get(position).getAverageBpmValue() <= 90)
        {
            holder.tvBpm.setTextColor(Color.rgb(255, 174, 0));
            holder.iv_heartbeat.setColorFilter(Color.rgb( 255, 174, 0));
        }

        if(records.get(position).getAverageBpmValue() > 90)
        {
            holder.tvBpm.setTextColor(Color.RED);
            holder.iv_heartbeat.setColorFilter(Color.RED);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), records.get(position).getAverageBpmValue().toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, RecordViewActivity.class);
                intent.putExtra("record", records.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvDuration, tvDate, tvBpm;
        ImageView iv_heartbeat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDuration = itemView.findViewById(R.id.fragment_records_regular_user_tv_duration);
            tvDate = itemView.findViewById(R.id.fragment_records_regular_user_tv_date);
            tvBpm = itemView.findViewById(R.id.fragment_records_regular_user_tv_bpm);
            iv_heartbeat = itemView.findViewById(R.id.fragment_records_regular_user_iv_heartbeat);
        }
    }

}
