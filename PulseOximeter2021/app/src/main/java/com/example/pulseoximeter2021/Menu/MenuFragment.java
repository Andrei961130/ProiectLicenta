package com.example.pulseoximeter2021.Menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pulseoximeter2021.About.AboutActivity;
import com.example.pulseoximeter2021.AddPatient.AddPatientActivity;
import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.LinkedPacients.LinkedPacientsActivity;
import com.example.pulseoximeter2021.Authenticate.AuthenticationActivity;
import com.example.pulseoximeter2021.LinkedPacients.LinkedPacientsAdapter;
import com.example.pulseoximeter2021.Profile.ProfileActivity;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Records.RecordsActivity;
import com.example.pulseoximeter2021.Services.FirebaseService;
import com.example.pulseoximeter2021.Settings.SettingsActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MenuFragment extends Fragment {

    ImageView ivMenuHeader;
    TextView tvMenuTitle;

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
            view = inflater.inflate(R.layout.fragment_menu_doctor, container, false);
            vNavigation = (NavigationView) view.findViewById(R.id.fragment_menu_navigation_view_doctor);
        }
        else
        {
            view = inflater.inflate(R.layout.fragment_menu, container, false);
            vNavigation = (NavigationView) view.findViewById(R.id.fragment_menu_navigation_view);
        }



        View headerLayout = vNavigation.getHeaderView(0);
//        View headerLayout = vNavigation.inflateHeaderView(R.layout.fragment_menu_header);
        ivMenuHeader = (ImageView) headerLayout.findViewById(R.id.fragment_menu_header_image);

        tvMenuTitle = headerLayout.findViewById(R.id.fragment_menu_header_tv_name);

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
                else if(id == R.id.menu_settings)
                {
                    Intent intent = new Intent(getContext(), SettingsActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.menu_logout)
                {
                    FirebaseAuth.getInstance().signOut();
//                    requireActivity().finish();
                    Intent intent = new Intent(getContext(), AuthenticationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("SIGN_OUT", true);
                    startActivity(intent);
                }
                else if(id == R.id.menu_patients)
                {
                    Intent intent = new Intent(getContext(), LinkedPacientsActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.menu_add_patient)
                {
                    Intent intent = new Intent(getContext(), AddPatientActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.menu_my_doctor)
                {
                    FirebaseService.getInstance().readDoctorByUser("", new FirebaseService.UserDataStatus() {
                        @Override
                        public void DataIsLoaded(ArrayList<User> users, ArrayList<String> keys) throws ExecutionException, InterruptedException {
//                            requireFragmentManager().popBackStack(new ProfileActivity());


                            Intent intent = new Intent(getContext(), ProfileActivity.class);
                            intent.putExtra("user", users.get(0));
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            //ActivityCompat.finishAffinity(profileActivity);
                        }

                        @Override
                        public void DataIsInserted() {

                        }
                    }, new FirebaseService.NoDoctorStatus() {
                        @Override
                        public void NoDoctor() {
                            Intent intent = new Intent(getContext(), ProfileActivity.class);
                            intent.putExtra("my_doctor_initial", true);
                            startActivity(intent);
                        }
                    });
                }
                if(id == R.id.menu_about)
                {
                    Intent intent = new Intent(getContext(), AboutActivity.class);
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

        if(FirebaseService.getInstance().getCurrentUser() != null)
            tvMenuTitle.setText(FirebaseService.getInstance().getCurrentUser().getDisplayName().split(" ")[0]);
        else
            tvMenuTitle.setText("");

//        int avatarSize = getResources().getDimensionPixelSize(R.dimen.mtrl_calendar_action_confirm_button_min_width);
//        String profilePhoto = getResources().getString(R.string.user_profile_photo);
//        Picasso.get()
//                .load(R.drawable.ic_choice)
//                .placeholder(R.drawable.img_circle_placeholder)
//                .resize(avatarSize, avatarSize)
//                .centerCrop()
//                .transform(new CircleTranformation())
//                .into(ivMenuHeader);

        if(!FirebaseService.getInstance().isDoctor())
            ivMenuHeader.setImageResource(R.drawable.ic_detailed_information);
    }
}