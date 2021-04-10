package com.example.pulseoximeter2021.MainScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.pulseoximeter2021.Bluetooth.BluetoothHelper;
import com.example.pulseoximeter2021.Measure.MeasureActivity;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Services.FirebaseService;

public class StartingFragment extends Fragment {

    BluetoothHelper bluetoothHelper;

    RadioGroup rgDuration;
    Button btnMeasure;
    int duration = 10;

    public interface OnBluetoothConnectionChangedListener {
        public void changeBluetoothIcon(Boolean connected);
    }

    OnBluetoothConnectionChangedListener listener;

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
        startBluetoothListener();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;
        if(activity instanceof OnBluetoothConnectionChangedListener) {
            listener = (OnBluetoothConnectionChangedListener) activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnBluetoothConnectionChangedListener interface");
        }
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
        bluetoothHelper.Connect("HC-05");
        if(!bluetoothHelper.isConnected())
        {
            Handler handler1 = new Handler(Looper.getMainLooper());
            handler1.postDelayed(() -> {
                bluetoothHelper.Connect("HC-05");

            }, 2000);
        }




//        int index = 1;
//        do
//        {
//            bluetoothHelper.Connect("HC-05");
//            if(bluetoothHelper.isConnected())
//                break;
//            index++;
//        }while (index != 5);
    }



    private void startBluetoothListener() {
        bluetoothHelper.setBluetoothHelperListener(new BluetoothHelper.BluetoothHelperListener() {
            @Override
            public void onBluetoothHelperMessageReceived(BluetoothHelper bluetoothhelper, String message) {

            }

            @Override
            public void onBluetoothHelperConnectionStateChanged(BluetoothHelper bluetoothhelper, boolean isConnected) {
                if (isConnected) {
                    listener.changeBluetoothIcon(true);
                } else {
                    listener.changeBluetoothIcon(false);
                }
            }
        });
    }
}