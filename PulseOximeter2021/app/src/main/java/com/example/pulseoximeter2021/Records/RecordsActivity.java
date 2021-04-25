package com.example.pulseoximeter2021.Records;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.R;

public class RecordsActivity extends AppCompatActivity {

    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        user = (User) getIntent().getSerializableExtra("user");
        RecordsFragment recordsFragment = new RecordsFragment();

        if(user != null)
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            recordsFragment.setArguments(bundle);
        }


        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_records_fragment_container, recordsFragment)
                .addToBackStack("RECORDS_FRAGMENT")
                .commit();
    }
}