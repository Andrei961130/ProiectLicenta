package com.example.pulseoximeter2021.Records;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
import com.example.pulseoximeter2021.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class RecordViewActivity extends AppCompatActivity {

    private Record record;

    private TextView tvDate;
    private TextView tvTime;
    private TextView tvRange;
    private TextView tvLength;
    private TextView tvAvg;
    private TextView tvOxygen;
    private TextView tvTemp;
    private TextInputEditText tiMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view2);

        Bundle extras = getIntent().getExtras();
        record = (Record) extras.getSerializable("record");

        initialiseViews();
        setupRecordInformation();
    }

    private void initialiseViews() {
        tvDate = findViewById(R.id.activity_record_view_tv_date);
        tvTime = findViewById(R.id.activity_record_view_tv_time);
        tvRange = findViewById(R.id.activity_record_view_tv_range);
        tvLength = findViewById(R.id.activity_record_view_tv_length);
        tvAvg = findViewById(R.id.activity_record_view_tv_average);
        tvOxygen = findViewById(R.id.activity_record_view_tv_oxygen);
        tvTemp = findViewById(R.id.activity_record_view_tv_temperature);
        tiMsg = findViewById(R.id.activity_record_view_ti_message);
    }

    private void setupRecordInformation() {
        tvDate.setText(record.getDateAndTime().split(" ")[0]);
        tvTime.setText(record.getDateAndTime().split(" ")[1]);
        tvRange.setText(String.format(Locale.ENGLISH, "%d - %d BPM", record.getMinBpmValue(), record.getMaxBpmValue()));
        tvLength.setText(String.format(Locale.ENGLISH, "%d seconds", record.getLength()));
        tvAvg.setText(String.format(Locale.ENGLISH, "%d BPM", record.getAverageBpmValue()));
        tvOxygen.setText(String.format(Locale.ENGLISH, "%d %%", record.getOxygen()));
        tvTemp.setText(String.format(Locale.ENGLISH, "%.1f", record.getTemperature()));

        if(record.getMessage() != "")
            tiMsg.setText(record.getMessage());
    }


}