package com.example.pulseoximeter2021.Records.RegularUserAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.R;

import java.util.ArrayList;


public class userAdapterTest extends RecyclerView.Adapter<userAdapterTest.ViewHolder> {

    private Context context;
    private ArrayList<User> users;

    public userAdapterTest(ArrayList<User> records) {
        this.users = records;
    }

    @NonNull
    @Override
    public userAdapterTest.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_records_user_test, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapterTest.ViewHolder holder, int position) {

        holder.tvName.setText(users.get(position).getFirstName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.fragment_records_regular_user_tv_name_test);
        }
    }

}
