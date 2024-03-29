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
import android.widget.Toast;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.DataLayer.Room.MyFirebaseDatabase;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class MeasureResultFragment extends Fragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private LineChart irChart;
    private LineChart bpmChart;
    private Typeface tfLight;

    private Record record;
    private int irSize;
    private List<Integer> irValues;
    private int bpmSize;
    private List<Integer> bpmValues;


    private Thread irThread;
    private Thread bpmThread;

    Boolean isRunning = false;

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

        irValues = record.getIrValues();
        irSize = irValues.size();

        bpmValues = record.getBpmValues();
        bpmSize = bpmValues.size();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_measure_result, container, false);

        irChart = view.findViewById(R.id.fragment_measure_result_ir_chart);
        bpmChart = view.findViewById(R.id.fragment_measure_result_bpm_chart);
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        tiEtComment = view.findViewById(R.id.fragment_measure_result_text_input_edit_text);

        tvOxygen = view.findViewById(R.id.fragment_measure_result_tv_oxygen);
        tvTemperature = view.findViewById(R.id.fragment_measure_result_tv_temperature);
        btnReplay = view.findViewById(R.id.fragment_measure_result_btn_replay);
        btnDelete = view.findViewById(R.id.fragment_measure_result_btn_delete);
        btnSave = view.findViewById(R.id.fragment_measure_result_btn_save);

        irChartSetup(irChart);
        bpmChartSetup(bpmChart);


//        replayButtonClick(view);

        isRunning = true;
        feedIrGraph(irThread, record.getLenghtAsMilis());
        feedBpmGraph(bpmThread, record.getLenghtAsMilis());

        btnReplay.setOnClickListener(this::replayButtonClick);
        btnDelete.setOnClickListener(this::deleteButtonClick);
        btnSave.setOnClickListener(this::saveButtonClick);

        tvOxygen.setText(String.format(Locale.ENGLISH, "%d %%", record.getOxygen()));
        tvTemperature.setText(String.format(Locale.ENGLISH, "%.1f", record.getTemperature()));

        return view;
    }

    private void saveButtonClick(View view) {

        record.setMessage(tiEtComment.getText().toString());

        FirebaseService.getInstance().addRecord(record, new MyFirebaseDatabase.DataStatus() {
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
//                Toast.makeText(requireActivity().getApplicationContext(), "Record saved", Toast.LENGTH_LONG).show();
                isRunning = false;
                requireActivity().finish();
            }

            @Override
            public void recordDataIsUpdated() {

            }

            @Override
            public void recordDataIsDeleted() {

            }
        });
    }

    private void deleteButtonClick(View view) {
        isRunning = false;
        requireActivity().finish();
    }

    private void replayButtonClick(View v) {
        if(!isRunning)
            isRunning = true;
        else
            return;

        if(bpmChart.getData() != null)
            bpmChart.getData().clearValues();

//        if(irChart.getData() != null)
//            irChart.getData().clearValues();

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
        bpmChart.getDescription().setEnabled(false);

        // enable touch gestures
        bpmChart.setTouchEnabled(false);

        // enable scaling and dragging
        bpmChart.setDragEnabled(false);
        bpmChart.setScaleEnabled(false);
        bpmChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        bpmChart.setPinchZoom(false);

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
        xl.setTextColor(Color.BLUE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(false);

        YAxis leftAxis = bpmChart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(getResources().getColor(R.color.blue_main));
//        leftAxis.setAxisMaximum(100f);
//        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);

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
        irChart.getDescription().setEnabled(false);
        irChart.getLegend().setEnabled(true);

        // enable touch gestures
        irChart.setTouchEnabled(false);

        // enable scaling and dragging
        irChart.setDragEnabled(false);
        irChart.setScaleEnabled(false);
        irChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        irChart.setPinchZoom(false);

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
        xl.setTextColor(Color.BLUE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(false);

        YAxis leftAxis = irChart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(Color.BLUE);
//        leftAxis.setAxisMaximum(100f);
//        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);

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
        set.setColor(ColorTemplate.rgb("FF5252"));
        set.setLineWidth(2f);
        set.setDrawCircles(false);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(getResources().getColor(R.color.blue_main));
        set.setValueTextColor(getResources().getColor(R.color.blue_main));
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        return set;
    }

    private LineDataSet createIrSet() {

        LineDataSet set = new LineDataSet(null, "Heartbeat");
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


    private void feedIrGraph(Thread thread, int length) {

        if (thread != null)
            thread.interrupt();

        int totalValues = record.getIrValues().size();
        int sleepMilis = length / totalValues;

        thread = new Thread(() -> {
            Runnable run;
            for (int index = 0; index < totalValues; index++) {
                run = new Runnable() {
                    int value;
                    Boolean isRunning;

                    public Runnable init(int value, Boolean isRunning) {
                        this.value = value;
                        this.isRunning = isRunning;
                        return this;
                    }

                    @Override
                    public void run() {
                        if(isAdded() && isRunning)
                            addIrEntry(value);
                        else
                            return;
                    }
                }.init(record.getIrValues().get(index), isRunning);

                // Don't generate garbage runnables inside the loop.
                if(isRunning)
                    requireActivity().runOnUiThread(run);
                else
                    return;

                if(index == totalValues-1)
                    isRunning = false;

                try {
                    Thread.sleep(sleepMilis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void feedBpmGraph(Thread thread, int length) {

        if (thread != null)
            thread.interrupt();

        int totalValues = record.getBpmValues().size();
        int sleepMilis = length / totalValues;


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
                        if(isAdded())
                            addBpmEntry(value);
                        else
                            return;
                    }
                }.init(record.getBpmValues().get(index));

                // Don't generate garbage runnables inside the loop.
                if(isRunning)
                    requireActivity().runOnUiThread(run);
                else
                    return;

                if(index == totalValues-1)
                    isRunning = false;

                try {
                    Thread.sleep(sleepMilis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}