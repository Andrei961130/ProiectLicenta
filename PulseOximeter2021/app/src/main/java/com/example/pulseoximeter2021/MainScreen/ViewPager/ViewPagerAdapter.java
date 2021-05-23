package com.example.pulseoximeter2021.MainScreen.ViewPager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulseoximeter2021.R;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    Context context;

    int[][] color_icon = new int[][]{
            {android.R.color.holo_red_light, R.drawable.ic_baseline_more_vert_24},
            {android.R.color.holo_blue_dark, R.drawable.ic_heart},
            {android.R.color.holo_purple, R.drawable.ic_menu_bars}
    };

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewPagerAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_page, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(color_icon[position][1]);
        holder.container.setBackgroundResource(color_icon[position][0]);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_page_imd_view);
            container = itemView.findViewById(R.id.item_page_container);
        }
    }
}
