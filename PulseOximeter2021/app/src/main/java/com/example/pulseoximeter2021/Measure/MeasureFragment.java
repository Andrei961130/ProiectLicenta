package com.example.pulseoximeter2021.Measure;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pulseoximeter2021.Bluetooth.ArduinoMessage;
import com.example.pulseoximeter2021.Bluetooth.BluetoothHelper;
import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
import com.example.pulseoximeter2021.NoValidReadFragment;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Services.FirebaseService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MeasureFragment extends Fragment {

    BluetoothHelper bluetoothHelper;

    private TextView detectedFinger;
    private TextView bpm;
    private TextView avgBpm;
    private TextView oxygen;
    private TextView tvTemperature;

    private LineChart chart;
    private Typeface tfLight;

    private int duration, index;
    Double temperature;
    Double temperatureOffset = 12.0;
    private List<Integer> irValues;
    private List<Integer> oxygenValues;
    private List<Integer> bpmValues;

    private ImageView decorator;
    private TextView decorator2;

    public MeasureFragment() {
        irValues = new ArrayList<>();
        bpmValues = new ArrayList<>();
        oxygenValues = new ArrayList<>();
        index = 0;
        temperature = 0.0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        duration = bundle.getInt("DURATION");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_measure, container, false);

        detectedFinger = (TextView) view.findViewById(R.id.detected_finger);
        bpm = (TextView) view.findViewById(R.id.bpm);
        avgBpm = (TextView) view.findViewById(R.id.avg_bpm);
        oxygen = (TextView) view.findViewById(R.id.oxygen);
        tvTemperature = (TextView) view.findViewById(R.id.temperature);
        chart = view.findViewById(R.id.fragment_measure_chart);

        decorator = view.findViewById(R.id.fragment_measure_decorator);
        decorator2 = view.findViewById(R.id.fragment_measure_decorator2);

        bluetoothHelper = BluetoothHelper.getInstance();
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        chartSetup(chart);
        bluetoothSetup(bluetoothHelper);

        //feedMultiple();
        startTimer();

        return view;
    }

    private void bluetoothSetup(BluetoothHelper bluetoothHelper) {

//        if(bluetoothHelper.isConnected())
//        {
//            if(bluetoothHelper.SendMessage("1\n"))
//            {
//                Toast.makeText(view.getContext(), "Message sent", Toast.LENGTH_SHORT).show();
//                //startTimer();
//            }
//            else
//            {
//                Toast.makeText(view.getContext(), "Message not sent", Toast.LENGTH_SHORT).show();
//            }
//        }
//        else
//        {
//            Toast.makeText(view.getContext(), "Not connected", Toast.LENGTH_SHORT).show();
//        }

        // Setup listener for Bluetooth helper;
        bluetoothHelper.setBluetoothHelperListener(new BluetoothHelper.BluetoothHelperListener() {
            @Override
            public void onBluetoothHelperMessageReceived(BluetoothHelper bluetoothhelper, String message) {
                // Do your stuff with the message received !!!
                ArduinoMessage arduinoMessage = ArduinoMessage.parseFromArduino(message);

                if(arduinoMessage == null)
                    return;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        if(arduinoMessage.getIsSensorStatusON() != null &&
//                                arduinoMessage.getIsSensorStatusON())
//                        {
//                            return;
//                        }
//                        else if(arduinoMessage.getIsSensorStatusON() != null &&
//                                !arduinoMessage.getIsSensorStatusON())
//                        {
//                            return;
//                        }

                        String detectedFingerStr = "Finger: " + arduinoMessage.getDetectedFinger().toString();
                        detectedFinger.setText(detectedFingerStr);

                        if(detectedFingerStr.contains("false"))
                        {
                            return;
                        }

                        Integer ir = arduinoMessage.getIrValue();

                        irValues.add(ir);
                        index++;

                        addEntry(ir);

                        if(!arduinoMessage.getOnlyIR())
                        {
                            String bpmStr = arduinoMessage.getBpm().toString();
                            bpm.setText(bpmStr);

                            Integer avgBpmInt = arduinoMessage.getAvgBpm();
                            String avgBpmStr = "Avg Bpm: " + avgBpmInt.toString();
                            avgBpm.setText(avgBpmInt.toString());

                            if(avgBpmInt > 99)
                            {
                                decorator.setVisibility(View.INVISIBLE);
                                decorator2.setVisibility(View.INVISIBLE);
                            }
                            else if(decorator.getVisibility() == View.INVISIBLE)
                            {
                                decorator.setVisibility(View.VISIBLE);
                                decorator2.setVisibility(View.VISIBLE);
                            }

                            bpmValues.add(avgBpmInt);

                            Integer oxygenInt = arduinoMessage.getOxygen();
                            if(oxygenInt > 100)
                                oxygenInt = 100;
                            String oxygenStr = oxygenInt.toString() + " %";
                            oxygen.setText(oxygenStr);

                            oxygenValues.add(oxygenInt);

                            Double temperatureDouble = arduinoMessage.getTemperature();
                            temperatureDouble = temperatureDouble - temperatureOffset;
                            String temperatureStr = String.format(Locale.ENGLISH, "%.1f", temperatureDouble);
                            tvTemperature.setText(temperatureStr);

                            temperature = temperatureDouble;
                        }
                    }
                });
            }

            @Override
            public void onBluetoothHelperConnectionStateChanged(BluetoothHelper bluetoothhelper, boolean isConnected) {
                if (isConnected) {
                    //Toast.makeText(view.getContext(), "Connected", Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(view.getContext(), "Not Connected", Toast.LENGTH_SHORT).show();
                    // Auto reconnect!
                    bluetoothHelper.Connect("HC-05");
                }
            }
        });
    }

    private void chartSetup(LineChart chart) {

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        chart.setAutoScaleMinMaxEnabled(true);


        // enable description text
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.TRANSPARENT);

        LineData data = new LineData();
        data.setValueTextColor(Color.RED);

        // add empty data
        chart.setData(data);

//         get the legend (only possible after setting data)
//        Legend l = chart.getLegend();

        // modify the legend ...
//        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(tfLight);
//        l.setTextColor(Color.WHITE);

        XAxis xl = chart.getXAxis();
        xl.setTypeface(tfLight);
        xl.setTextColor(Color.BLUE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(Color.BLUE);
//        leftAxis.setAxisMaximum(100f);
//        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void startTimer() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        openMeasuringFragment();
                    }
                },
                duration * 1000
        );
    }

    private void openMeasuringFragment() {

        if(bpmValues.isEmpty())
        {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_measure_fragment_container, new NoValidReadFragment())
                    .addToBackStack("NO_VALID_READ_FRAGMENT")
                    .commit();

            return;
        }


        Collections.sort(oxygenValues);
        double mediumOxygen;

//        if(oxygenValues.isEmpty()) {
//            oxygenValues.add(0);
//            oxygenValues.add(0);
//        }

        if (oxygenValues.size() % 2 == 0)
            mediumOxygen = ((double) oxygenValues.get(oxygenValues.size() / 2) + (double) oxygenValues.get(oxygenValues.size() / 2 - 1))/2;
        else
            mediumOxygen = (double) oxygenValues.get(oxygenValues.size() / 2);



        String fullName = FirebaseService.getInstance().getFullName();

        Calendar cal = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:MM");

        String date = sdf.format(cal.getTime());
        Record record = new Record(duration, irValues, bpmValues, (int) mediumOxygen, temperature, "", date, fullName);

        Bundle bundle = new Bundle();
        bundle.putSerializable("record", record);

        MeasureResultFragment measureResultFragment = new MeasureResultFragment();
        measureResultFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_measure_fragment_container, measureResultFragment)
                .addToBackStack("MEASURE_RESULT_FRAGMENT")
                .commit();
    }


    Random rand = new Random();

    private void addEntry() {

        LineData data = chart.getData();

        if (data != null) {

            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            Entry entry = new Entry(set.getEntryCount(), (float) (rand.nextInt(10000-2000)+2000));
            data.addEntry(entry, 0);
            //data.addEntry(new Entry(set.getEntryCount() + 2, (float) (Math.random() * 10000) + 30f), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            chart.notifyDataSetChanged();

            // limit the number of visible entries
            chart.setVisibleXRangeMaximum(100);
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            chart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private void addEntry(int y) {

        LineData data = chart.getData();

        if (data != null) {

            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            Entry entry = new Entry(set.getEntryCount(), (float) y);
            data.addEntry(entry, 0);
            //data.addEntry(new Entry(set.getEntryCount() + 2, (float) (Math.random() * 10000) + 30f), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            chart.notifyDataSetChanged();

            // limit the number of visible entries
            chart.setVisibleXRangeMaximum(50);
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            chart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(getResources().getColor(R.color.blue_main));
        set.setLineWidth(2f);
        set.setDrawCircles(false);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private Thread thread;

    private void feedMultiple() {

        if (thread != null)
            thread.interrupt();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                addEntry();
            }
        };

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 200; i++) {

                    // Don't generate garbage runnables inside the loop.
                    getActivity().runOnUiThread(runnable);

                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }
}