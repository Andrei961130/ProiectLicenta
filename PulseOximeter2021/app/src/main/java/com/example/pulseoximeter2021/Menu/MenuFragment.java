package com.example.pulseoximeter2021.Menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pulseoximeter2021.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class MenuFragment extends Fragment {

    ImageView ivMenuHeader;
    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

//        ivMenuHeader = (ImageView) view.findViewById(R.id.fragment_menu_header_image);

        NavigationView vNavigation = (NavigationView) view.findViewById(R.id.fragment_menu_navigation_view);

        View headerLayout = vNavigation.getHeaderView(0);
//        View headerLayout = vNavigation.inflateHeaderView(R.layout.fragment_menu_header);
        ivMenuHeader = (ImageView) headerLayout.findViewById(R.id.fragment_menu_header_image);


        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(getActivity(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                return false;
            }
        }) ;

        setupHeader();
        return view;
    }

    private void setupHeader() {
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.mtrl_calendar_action_confirm_button_min_width);
        String profilePhoto = getResources().getString(R.string.user_profile_photo);
        Picasso.get()
                .load(profilePhoto)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTranformation())
                .into(ivMenuHeader);
    }
}