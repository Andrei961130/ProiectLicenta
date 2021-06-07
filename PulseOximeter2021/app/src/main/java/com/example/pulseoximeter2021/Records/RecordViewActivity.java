package com.example.pulseoximeter2021.Records;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
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
    private Button btnReplay;

    private LineChart irChart;
    private LineChart bpmChart;
    private Typeface tfLight;

    private Thread irThread;
    private Thread bpmThread;

    Boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view2);

        Bundle extras = getIntent().getExtras();
        record = (Record) extras.getSerializable("record");

        initialiseViews();
        setupRecordInformation();

        irChartSetup(irChart);
        bpmChartSetup(bpmChart);

        isRunning = true;
        feedIrGraph(irThread, record.getLenghtAsMilis());
        feedBpmGraph(bpmThread, record.getLenghtAsMilis());

        btnReplay.setOnClickListener(this::replayButtonClick);
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
        btnReplay = findViewById(R.id.activity_record_view_btn_replay);
        irChart = findViewById(R.id.activity_record_view_ir_chart);
        bpmChart = findViewById(R.id.activity_record_view_bpm_chart);

        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
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
                        if(isRunning)
                            addIrEntry(value);
                        else
                            return;
                    }
                }.init(record.getIrValues().get(index), isRunning);

                // Don't generate garbage runnables inside the loop.
                if(isRunning)
                    runOnUiThread(run);
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
                    Boolean isRunning;

                    public Runnable init(int value, Boolean isRunning) {
                        this.value = value;
                        this.isRunning = isRunning;
                        return this;
                    }

                    @Override
                    public void run() {
                        if(isRunning)
                            addBpmEntry(value);
                        else
                            return;
                    }
                }.init(record.getBpmValues().get(index), isRunning);

                // Don't generate garbage runnables inside the loop.
                if(isRunning)
                    runOnUiThread(run);
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

    private void addBpmEntry(int value) {
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

}