package com.example.pulseoximeter2021.LinkedPacients;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.Profile.ProfileActivity;
import com.example.pulseoximeter2021.R;

import java.util.ArrayList;

public class LinkedPacientsAdapter extends RecyclerView.Adapter<LinkedPacientsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<User> users;

    public LinkedPacientsAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_card_view, parent, false);
        return new LinkedPacientsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvDisplayName.setText(users.get(position).getFirstName().concat(users.get(position).getLastName()));
        holder.tvEmail.setText(users.get(position).getEmail());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), users.get(position).getFirstName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user", users.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvDisplayName, tvEmail;
        ImageView ivGender;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDisplayName = itemView.findViewById(R.id.card_view_tv_display_name);
            tvEmail = itemView.findViewById(R.id.card_view_tv_email);
            ivGender = itemView.findViewById(R.id.card_view_iv_gender);
        }
    }
}
