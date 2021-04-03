package com.example.pulseoximeter2021.Records.RegularUserAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
import com.example.pulseoximeter2021.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class RegularUserAdapter  extends FirebaseRecyclerAdapter<
        Record, RegularUserAdapter.RegularUserViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RegularUserAdapter(@NonNull FirebaseRecyclerOptions<Record> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RegularUserViewHolder holder, int position, @NonNull Record model) {

        holder.tvName.setText(model.getFullName());
        //holder.tvDate.setText(model.getDateAndTimeAsString());
        //holder.tvBpm.setText(model.getAverageBpmValue());
    }

    @NonNull
    @Override
    public RegularUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_records_regular_user, parent, false);
        return new RegularUserViewHolder(view);
    }

    class RegularUserViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDate, tvBpm;

        public RegularUserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.fragment_records_regular_user_tv_name);
            tvDate = itemView.findViewById(R.id.fragment_records_regular_user_tv_date);
            tvBpm = itemView.findViewById(R.id.fragment_records_regular_user_tv_bpm);
        }
    }
}
