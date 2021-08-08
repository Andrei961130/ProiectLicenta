package com.example.pulseoximeter2021.Measure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pulseoximeter2021.R;

public class MeasureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        addMeasureFragment();
    }

    private void addMeasureFragment() {
        int duration = getIntent().getIntExtra("DURATION", 10);
        Bundle bundle = new Bundle();
        bundle.putInt("DURATION", duration);

        MeasureFragment measureFragment = new MeasureFragment();
        measureFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_measure_fragment_container, measureFragment)
                .addToBackStack("MEASURE_FRAGMENT")
                .commit();
    }
}