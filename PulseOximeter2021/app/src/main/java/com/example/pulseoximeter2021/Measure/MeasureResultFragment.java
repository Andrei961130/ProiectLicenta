package com.example.pulseoximeter2021.Measure;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pulseoximeter2021.DataLayer.Models.Record;
import com.example.pulseoximeter2021.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.google.android.gms.common.internal.IResolveAccountCallbacks;


public class MeasureResultFragment extends Fragment {

    private LineChart irChart;
    private LineChart bpmChart;
    private Typeface tfLight;

    private Record record;


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

        return view;
    }

    private void bpmChartSetup(LineChart bpmChart) {

    }

    private void irChartSetup(LineChart irChart) {

    }
}