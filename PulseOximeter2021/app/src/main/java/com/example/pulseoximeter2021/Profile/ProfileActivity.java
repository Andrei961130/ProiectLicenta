package com.example.pulseoximeter2021.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Link;
import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.MainScreen.StartingFragment;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Services.FirebaseService;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ProfileActivity extends AppCompatActivity {

    User user = null;
    Boolean doctorView = false;
    User doctorForUser = null;
    Boolean hasDoctor = false;

    Boolean myDoctorInitial;

    TextView tvFullName;
    TextView tvEmail;
    TextView tvPhoneNumber;
    TextView tvBirthday;
    ImageView ivPhoto;
    ImageView ivArrowBack;
    Button btnLink;

    NoDoctorFragment noDoctorFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivPhoto = findViewById(R.id.activity_profile_iv_photo);
        tvFullName = findViewById(R.id.activity_profile_tv_name);
        tvEmail = findViewById(R.id.activity_profile_tv_email);
        tvPhoneNumber = findViewById(R.id.activity_profile_tv_phone_number);
        tvBirthday = findViewById(R.id.activity_profile_tv_birthday);
        ivArrowBack = findViewById(R.id.activity_profile_iv_arrow_back);
        btnLink = findViewById((R.id.activity_profile_btn_link));

        user = (User) getIntent().getSerializableExtra("user");
        doctorView = (Boolean) getIntent().getSerializableExtra("doctor_view");

        if(doctorView == null)
            doctorView = false;

        myDoctorInitial = (Boolean) getIntent().getSerializableExtra("my_doctor_initial");

        if(myDoctorInitial == null)
            myDoctorInitial = false;

        setLinkButton();

        setUserDetails(user);

        ivArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setLinkButton() {
        btnLink.setVisibility(View.INVISIBLE);

        if(doctorView) {
            FirebaseService.getInstance().readDoctorByUser(user.getUid(), new FirebaseService.UserDataStatus() {
                @Override
                public void DataIsLoaded(ArrayList<User> users, ArrayList<String> keys) throws ExecutionException, InterruptedException {
                    doctorForUser = users.get(0);
                    btnLink.setText("Already linked");
                    hasDoctor = true;

                    if (noDoctorFragment != null) {
                        getSupportFragmentManager().popBackStack();
                        noDoctorFragment = null;
                    }
                }

                @Override
                public void DataIsInserted() {

                }
            }, new FirebaseService.NoDoctorStatus() {
                @Override
                public void NoDoctor() {

                }
            });
            btnLink.setVisibility(View.VISIBLE);
            btnLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!hasDoctor)
                    {
                        Link link = new Link();
                        link.setDoctorUid(FirebaseService.getInstance().getCurrentUser().getUid());
                        link.setPacientUid(user.getUid());
                        FirebaseService.getInstance().addUserAndDoctorLink(link, new FirebaseService.UserDataStatus() {
                            @Override
                            public void DataIsLoaded(ArrayList<User> users, ArrayList<String> keys) throws ExecutionException, InterruptedException {

                            }

                            @Override
                            public void DataIsInserted() {
                                btnLink.setText("Already linked");
                                hasDoctor = true;

                                if(noDoctorFragment != null) {
                                    getSupportFragmentManager().popBackStack();
                                    noDoctorFragment = null;
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void setUserDetails(User user) {

        if(user==null && myDoctorInitial)
        {
            noDoctorFragment = new NoDoctorFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_profile_fragment_container, noDoctorFragment)
                    .commit();

            return;
        }

        if(user == null)
        {
            user = FirebaseService.getInstance().getUserDetails();
//            tvFullName.setText(FirebaseService.getInstance().getFullName());
        }

        tvFullName.setText(user.getFirstName().concat(" ").concat(user.getLastName()));
        tvEmail.setText(user.getEmail());
        tvPhoneNumber.setText(user.getPhoneNumber());
        tvBirthday.setText(user.getBirthday());
    }
}