package com.example.pulseoximeter2021.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Services.FirebaseService;

public class ProfileActivity extends AppCompatActivity {

    User user = null;
    Boolean doctorView = false;

    TextView tvFullName;
    TextView tvEmail;
    TextView tvPhoneNumber;
    TextView tvBirthday;
    ImageView ivPhoto;
    ImageView ivArrowBack;

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

        user = (User) getIntent().getSerializableExtra("user");
        doctorView = (Boolean) getIntent().getSerializableExtra("doctor_view");

        setUserDetails(user);

        ivArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUserDetails(User user) {

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