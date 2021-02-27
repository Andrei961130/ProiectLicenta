package com.example.pulseoximeter2021.Measure;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pulseoximeter2021.DataLayer.Models.Record;
import com.example.pulseoximeter2021.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;


public class MeasureResultFragment extends Fragment {

    private LineChart irChart;
    private LineChart bpmChart;
    private Typeface tfLight;

    private Record record;

    private Thread irThread;
    private Thread bpmThread;


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


        irChartSetup(irChart);
        bpmChartSetup(bpmChart);

//        for (int i :
//                record.getIrValues()) {
//            addIrEntry(i);
//        }

        for (Pair<Integer, Integer> i :
                record.getBpmValues()) {
            addBpmEntry(i.second);
        }


//        irChart.setVisibleXRangeMaximum(50);
//        irChart.moveViewToX(irChart.getData().getEntryCount());
//        irChart.animateX(record.getLenghtAsMilis());
        bpmChart.animateX(record.getLenghtAsMilis());
//        bpmChart.moveViewToX(bpmChart.getData().getEntryCount());

        feedIrGraph(irThread, record.getLenghtAsMilis());


        return view;
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

        int totalIrValues = record.getIrValues().size();
        int sleepMilis = lenght / totalIrValues;

        Runnable runnable = new Runnable() {
            int value;

            public Runnable init(int value) {
                this.value = value;
                return this;
            }

            @Override
            public void run() {
                addIrEntry(value);
            }
        };

        thread = new Thread(() -> {
            Runnable run;
            for (int index = 0; index < totalIrValues; index++) {
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
                getActivity().runOnUiThread(run);

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