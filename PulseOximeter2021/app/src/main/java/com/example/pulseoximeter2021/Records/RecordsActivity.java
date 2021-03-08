package com.example.pulseoximeter2021.Records;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pulseoximeter2021.R;

public class RecordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_records_fragment_container, new RecordsFragment())
                .addToBackStack("RECORDS_FRAGMENT")
                .commit();
    }
}