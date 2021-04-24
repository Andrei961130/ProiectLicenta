package com.example.pulseoximeter2021.Menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pulseoximeter2021.Authenticate.AuthenticationActivity;
import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.Profile.ProfileActivity;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Records.RecordsActivity;
import com.example.pulseoximeter2021.Services.FirebaseService;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        View view = null;

//        ivMenuHeader = (ImageView) view.findViewById(R.id.fragment_menu_header_image);

        NavigationView vNavigation;

        if(FirebaseService.getInstance().isDoctor())
        {
            view = inflater.inflate(R.layout.fragment_menu, container, false);
            vNavigation = (NavigationView) view.findViewById(R.id.fragment_menu_navigation_view);
        }
        else
        {
            view = inflater.inflate(R.layout.fragment_menu, container, false);
            vNavigation = (NavigationView) view.findViewById(R.id.fragment_menu_navigation_view);
        }



        View headerLayout = vNavigation.getHeaderView(0);
//        View headerLayout = vNavigation.inflateHeaderView(R.layout.fragment_menu_header);
        ivMenuHeader = (ImageView) headerLayout.findViewById(R.id.fragment_menu_header_image);

//        View menu = vNavigation.findViewById(drawer_menu);
//        headerLayout.setBackgroundColor(getResources().getColor(R.color.green));

        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                Fragment fragment = null;

                if(id == R.id.menu_profile)
                {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.menu_records)
                {
                    Intent intent = new Intent(getContext(), RecordsActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.menu_logout)
                {
                    FirebaseAuth.getInstance().signOut();
//                    requireActivity().finish();
                    Intent intent = new Intent(getContext(), AuthenticationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

//                getFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.activity_measure_fragment_container, new MeasureFragment())
//                        .addToBackStack("MEASURE_FRAGMENT")
//                        .commit();

//                Toast.makeText(getActivity(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
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