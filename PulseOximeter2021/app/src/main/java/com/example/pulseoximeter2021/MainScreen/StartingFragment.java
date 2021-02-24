package com.example.pulseoximeter2021.MainScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.pulseoximeter2021.Bluetooth.BluetoothHelper;
import com.example.pulseoximeter2021.Measure.MeasureActivity;
import com.example.pulseoximeter2021.R;

public class StartingFragment extends Fragment {

    BluetoothHelper bluetoothHelper;

    RadioGroup rgDuration;
    Button btnMeasure;
    int duration = 10;

    public StartingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_starting, container, false);

        rgDuration = view.findViewById(R.id.fragment_starting_radio_group);
        btnMeasure = view.findViewById(R.id.fragment_starting_btn_measure);

        rgDuration.setOnCheckedChangeListener(this::rgDurationOnCheckedChanged);
        btnMeasure.setOnClickListener(this::measureClick);

        startBluetoothConnection();

        return view;
    }

    private void measureClick(View view) {
        Intent intent = new Intent(getContext(), MeasureActivity.class);
        intent.putExtra("DURATION", duration);
        startActivity(intent);
    }

    private void rgDurationOnCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.fragment_starting_rb_10:
                duration = 10;
                break;
            case R.id.fragment_starting_rb_15:
                duration = 15;
                break;
            case R.id.fragment_starting_rb_20:
                duration = 20;
                break;
        }
    }

    private void startBluetoothConnection() {

        bluetoothHelper = BluetoothHelper.getInstance();

        int index = 1;
        do
        {
            bluetoothHelper.Connect("HC-05");
            if(bluetoothHelper.isConnected())
                break;
            index++;
        }while (index != 5);
    }
}