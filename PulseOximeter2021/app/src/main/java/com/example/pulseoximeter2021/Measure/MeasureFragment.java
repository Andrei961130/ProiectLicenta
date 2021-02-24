package com.example.pulseoximeter2021.Measure;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pulseoximeter2021.Bluetooth.ArduinoMessage;
import com.example.pulseoximeter2021.Bluetooth.BluetoothHelper;
import com.example.pulseoximeter2021.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.Random;

public class MeasureFragment extends Fragment {

    BluetoothHelper bluetoothHelper;

    private TextView detectedFinger;
    private TextView irValue;
    private TextView onlyIR;
    private TextView bpm;
    private TextView avgBpm;
    private TextView oxygen;
    private TextView temperature;

    private LineChart chart;
    private Typeface tfLight;

    public MeasureFragment() {
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
        View view = inflater.inflate(R.layout.fragment_measure, container, false);

        detectedFinger = (TextView) view.findViewById(R.id.detected_finger);
        irValue = (TextView) view.findViewById(R.id.ir_value);
        onlyIR = (TextView) view.findViewById(R.id.only_ir);
        bpm = (TextView) view.findViewById(R.id.bpm);
        avgBpm = (TextView) view.findViewById(R.id.avg_bpm);
        oxygen = (TextView) view.findViewById(R.id.oxygen);
        temperature = (TextView) view.findViewById(R.id.temperature);

        bluetoothHelper = BluetoothHelper.getInstance();
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");


        chart = view.findViewById(R.id.fragment_measure_chart);
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
        chart.getDescription().setEnabled(true);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

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
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(Color.WHITE);
//        leftAxis.setAxisMaximum(100f);
//        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
//
//        int index = 1;
//        do
//        {
//            bluetoothHelper.Connect("HC-05");
//            if(bluetoothHelper.isConnected())
//                break;
//
//            index++;
//        }while (index != 5);

        if(bluetoothHelper.isConnected())
        {
            if(bluetoothHelper.SendMessage("1\n"))
            {
                Toast.makeText(view.getContext(), "Message sent", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(view.getContext(), "Message not sent", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(view.getContext(), "Not connected", Toast.LENGTH_SHORT).show();
        }

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

                        String onlyIRStr = "Only IR: " + arduinoMessage.getOnlyIR().toString();
                        onlyIR.setText(onlyIRStr);

                        String irValueStr = "IRValue: " + arduinoMessage.getIrValue().toString();
                        irValue.setText(irValueStr);

                        addEntry(arduinoMessage.getIrValue());

                        if(!arduinoMessage.getOnlyIR())
                        {
                            String bpmStr = "Bpm: " + arduinoMessage.getBpm().toString();
                            bpm.setText(bpmStr);

                            String avgBpmStr = "Avg Bpm: " + arduinoMessage.getAvgBpm().toString();
                            avgBpm.setText(avgBpmStr);

                            String oxygenStr = "Oxygen: " + arduinoMessage.getOxygen().toString() + " %";
                            oxygen.setText(oxygenStr);

                            String temperatureStr = "Temperature: " + arduinoMessage.getTemperature().toString() + " C";
                            temperature.setText(oxygenStr);
                        }



                    }
                });
            }

            @Override
            public void onBluetoothHelperConnectionStateChanged(BluetoothHelper bluetoothhelper, boolean isConnected) {
                if (isConnected) {
                    Toast.makeText(view.getContext(), "Connected", Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(view.getContext(), "Not Connected", Toast.LENGTH_SHORT).show();
                    // Auto reconnect!
                    bluetoothHelper.Connect("HC-05");
                }
            }
        });

        //feedMultiple();

        return view;
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
        set.setColor(ColorTemplate.getHoloBlue());
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
                for (int i = 0; i < 1000; i++) {

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