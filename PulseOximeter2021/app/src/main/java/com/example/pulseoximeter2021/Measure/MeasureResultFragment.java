package com.example.pulseoximeter2021.Measure;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.DataLayer.Room.MyFirebaseDatabase;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MeasureResultFragment extends Fragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private LineChart irChart;
    private LineChart bpmChart;
    private Typeface tfLight;

    private Record record;

    private Thread irThread;
    private Thread bpmThread;

    private TextView tvOxygen;
    private TextView tvTemperature;
    private TextInputEditText tiEtComment;
    private Button btnReplay;
    private Button btnDelete;
    private Button btnSave;


    public MeasureResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        record = (Record) bundle.getSerializable("record");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_measure_result, container, false);

        irChart = view.findViewById(R.id.fragment_measure_result_ir_chart);
        bpmChart = view.findViewById(R.id.fragment_measure_result_bpm_chart);
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        tvOxygen = view.findViewById(R.id.fragment_measure_result_tv_oxygen);
        tvTemperature = view.findViewById(R.id.fragment_measure_result_tv_temperature);
        btnReplay = view.findViewById(R.id.fragment_measure_result_btn_replay);
        btnDelete = view.findViewById(R.id.fragment_measure_result_btn_delete);
        btnSave = view.findViewById(R.id.fragment_measure_result_btn_save);

        irChartSetup(irChart);
        bpmChartSetup(bpmChart);


        replayButtonClick(view);

        btnReplay.setOnClickListener(this::replayButtonClick);
        btnDelete.setOnClickListener(this::deleteButtonClick);
        btnSave.setOnClickListener(this::saveButtonClick);

//        for (int i :
//                record.getIrValues()) {
//            addIrEntry(i);
//        }

//        for (int i :
//                record.getBpmValues()) {
//            addBpmEntry(i);
//        }

        return view;
    }

    private void saveButtonClick(View view) {

        String uid = firebaseAuth.getUid();
        String key = databaseReference.child("Records").child(uid).push().getKey();
        databaseReference.child("Records").child(uid).child(key).setValue(record);

//        AddRecordInFireBase addRecordInFireBase = new AddRecordInFireBase();
//        addRecordInFireBase.execute(record);

        requireActivity().finish();
    }

    private void deleteButtonClick(View view) {

        requireActivity().finish();
    }

    private void replayButtonClick(View v) {
        feedIrGraph(irThread, record.getLenghtAsMilis());
        feedBpmGraph(bpmThread, record.getLenghtAsMilis());
    } 

    private void bpmChartSetup(LineChart bpmChart) {

        bpmChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        bpmChart.setAutoScaleMinMaxEnabled(true);


        // enable description text
        bpmChart.getDescription().setEnabled(true);

        // enable touch gestures
        bpmChart.setTouchEnabled(true);

        // enable scaling and dragging
        bpmChart.setDragEnabled(true);
        bpmChart.setScaleEnabled(true);
        bpmChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        bpmChart.setPinchZoom(true);

        // set an alternative background color
        bpmChart.setBackgroundColor(Color.TRANSPARENT);

        LineData data = new LineData();
        data.setValueTextColor(Color.RED);

        // add empty data
        bpmChart.setData(data);

//         get the legend (only possible after setting data)
//        Legend l = chart.getLegend();

        // modify the legend ...
//        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(tfLight);
//        l.setTextColor(Color.WHITE);

        XAxis xl = bpmChart.getXAxis();
        xl.setTypeface(tfLight);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = bpmChart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(Color.WHITE);
//        leftAxis.setAxisMaximum(100f);
//        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = bpmChart.getAxisRight();
        rightAxis.setEnabled(false);


        // move to the first entry
        bpmChart.moveViewToX(0);
    }

    private void addBpmEntry(int value)
    {
        LineData data = bpmChart.getData();

        if (data != null) {

            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createBpmSet();
                data.addDataSet(set);
            }

            Entry entry = new Entry(set.getEntryCount(), (float) value);
            data.addEntry(entry, 0);
            //data.addEntry(new Entry(set.getEntryCount() + 2, (float) (Math.random() * 10000) + 30f), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            bpmChart.notifyDataSetChanged();

            // limit the number of visible entries
            //bpmChart.setVisibleXRangeMaximum(50);
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            bpmChart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private void irChartSetup(LineChart irChart) {

        irChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        irChart.setAutoScaleMinMaxEnabled(true);


        // enable description text
        irChart.getDescription().setEnabled(true);

        // enable touch gestures
        irChart.setTouchEnabled(true);

        // enable scaling and dragging
        irChart.setDragEnabled(true);
        irChart.setScaleEnabled(true);
        irChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        irChart.setPinchZoom(true);

        // set an alternative background color
        irChart.setBackgroundColor(Color.TRANSPARENT);

        LineData data = new LineData();
        data.setValueTextColor(Color.RED);

        // add empty data
        irChart.setData(data);

//         get the legend (only possible after setting data)
//        Legend l = chart.getLegend();

        // modify the legend ...
//        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(tfLight);
//        l.setTextColor(Color.WHITE);

        XAxis xl = irChart.getXAxis();
        xl.setTypeface(tfLight);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = irChart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(Color.WHITE);
//        leftAxis.setAxisMaximum(100f);
//        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = irChart.getAxisRight();
        rightAxis.setEnabled(false);


        // move to the first entry
        irChart.moveViewToX(0);
    }

    private void addIrEntry(int value) {

        LineData data = irChart.getData();

        if (data != null) {

            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createIrSet();
                data.addDataSet(set);
            }

            Entry entry = new Entry(set.getEntryCount(), (float) value);
            data.addEntry(entry, 0);
            //data.addEntry(new Entry(set.getEntryCount() + 2, (float) (Math.random() * 10000) + 30f), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            irChart.notifyDataSetChanged();

            // limit the number of visible entries
            irChart.setVisibleXRangeMaximum(50);
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            irChart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createBpmSet() {

        LineDataSet set = new LineDataSet(null, "Heart rate per minute");
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

    private LineDataSet createIrSet() {

        LineDataSet set = new LineDataSet(null, "Heartbeat");
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


    private void feedIrGraph(Thread thread, int lenght) {

        if (thread != null)
            thread.interrupt();

        int totalValues = record.getIrValues().size();
        int sleepMilis = lenght / totalValues;

//        Runnable runnable = new Runnable() {
//            int value;
//
//            public Runnable init(int value) {
//                this.value = value;
//                return this;
//            }
//
//            @Override
//            public void run() {
//                addIrEntry(value);
//            }
//        };

        thread = new Thread(() -> {
            Runnable run;
            for (int index = 0; index < totalValues; index++) {
                run = new Runnable() {
                        int value;

                        public Runnable init(int value) {
                            this.value = value;
                            return this;
                        }

                        @Override
                        public void run() {
                            addIrEntry(value);
                        }
                    }.init(record.getIrValues().get(index));

                // Don't generate garbage runnables inside the loop.
                requireActivity().runOnUiThread(run);

                try {
                    Thread.sleep(sleepMilis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void feedBpmGraph(Thread thread, int lenght) {

        if (thread != null)
            thread.interrupt();

        int totalValues = record.getBpmValues().size();
        int sleepMilis = lenght / totalValues;

//        Runnable runnable = new Runnable() {
//            int value;
//
//            public Runnable init(int value) {
//                this.value = value;
//                return this;
//            }
//
//            @Override
//            public void run() {
//                addBpmEntry(value);
//            }
//        };

        thread = new Thread(() -> {
            Runnable run;
            for (int index = 0; index < totalValues; index++) {
                run = new Runnable() {
                    int value;

                    public Runnable init(int value) {
                        this.value = value;
                        return this;
                    }

                    @Override
                    public void run() {
                        addBpmEntry(value);
                    }
                }.init(record.getIrValues().get(index));

                // Don't generate garbage runnables inside the loop.
                requireActivity().runOnUiThread(run);

                try {
                    Thread.sleep(sleepMilis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private class AddRecordInFireBase extends AsyncTask<Record,Void,Void>
    {
        @Override
        protected Void doInBackground(Record... records) {
            new MyFirebaseDatabase().addRecord(firebaseAuth.getUid(), records[0], new MyFirebaseDatabase.DataStatus() {
                @Override
                public void userDataIsLoaded(ArrayList<User> users, ArrayList<String> keys) throws ExecutionException, InterruptedException {
                    
                }

                @Override
                public void userDataIsInserted() {

                }

                @Override
                public void userDataIsUpdated() {

                }

                @Override
                public void userDataIsDeleted() {

                }

                @Override
                public void recordDataIsLoaded(ArrayList<Record> records, ArrayList<String> keys) throws ExecutionException, InterruptedException {

                }

                @Override
                public void recordDataIsInserted() {

                }

                @Override
                public void recordDataIsUpdated() {

                }

                @Override
                public void recordDataIsDeleted() {

                }
            });
            return null;
        }
    }
}