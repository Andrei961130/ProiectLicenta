package com.example.pulseoximeter2021.Records;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.R;
//import com.example.pulseoximeter2021.Records.RegularUserAdapter.RegularUserAdapter;
import com.example.pulseoximeter2021.Records.RegularUserAdapter.RegularUserAdapter2;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.example.pulseoximeter2021.Services.FirebaseService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecordsFragment extends Fragment {

    private User user = null;
    private String uid = null;

    private RecyclerView recyclerView;
    ArrayList<Record> recordsList;

    private RegularUserAdapter2 userAdapter2;

    private BarChart barChart;
    private ArrayList<BarEntry> greenRecords;
    private ArrayList<BarEntry> orangeRecords;
    private ArrayList<BarEntry> redRecords;

    public RecordsFragment() {
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle != null)
            user = (User) bundle.getSerializable("user");

        if(user != null)
            uid = user.getUid();
        else
            uid = FirebaseService.getInstance().getCurrentUser().getUid();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records_list2, container, false);

        barChart = view.findViewById(R.id.fragment_records_list_bar_chart);

        greenRecords = new ArrayList<>(7);
        orangeRecords = new ArrayList<>(7);
        redRecords = new ArrayList<>(7);


        greenRecords.add(new BarEntry(1,2));
        greenRecords.add(new BarEntry(2,2));
        greenRecords.add(new BarEntry(3,2));
        greenRecords.add(new BarEntry(4,2));
        greenRecords.add(new BarEntry(5,2));
        greenRecords.add(new BarEntry(6,2));
        greenRecords.add(new BarEntry(7,2));

        orangeRecords.add(new BarEntry(1,3));
        orangeRecords.add(new BarEntry(2,3));
        orangeRecords.add(new BarEntry(3,3));
        orangeRecords.add(new BarEntry(4,3));
        orangeRecords.add(new BarEntry(5,3));
        orangeRecords.add(new BarEntry(6,3));
        orangeRecords.add(new BarEntry(7,3));

        redRecords.add(new BarEntry(1,4));
        redRecords.add(new BarEntry(2,4));
        redRecords.add(new BarEntry(3,4));
        redRecords.add(new BarEntry(4,4));
        redRecords.add(new BarEntry(5,4));
        redRecords.add(new BarEntry(6,4));
        redRecords.add(new BarEntry(7,4));


        BarDataSet barDataSetGreen = new BarDataSet(greenRecords,"Green records");
        barDataSetGreen.setColor(Color.GREEN);
        BarDataSet barDataSetOrange = new BarDataSet(orangeRecords,"Orange records");
        barDataSetOrange.setColor(Color.rgb(255, 174, 0));
        BarDataSet barDataSetRed = new BarDataSet(redRecords,"Red records");
        barDataSetRed.setColor(Color.RED);


        BarData barData = new BarData();
        barData.addDataSet(barDataSetGreen);
        barData.addDataSet(barDataSetOrange);
        barData.addDataSet(barDataSetRed);

        barChart.setData(barData);

        barData.setBarWidth(0.2f);
        barData.groupBars(0f,0.4f,0);

        barChart.setBackgroundColor(Color.TRANSPARENT);

        barChart.getAxisLeft().setAxisMaximum(5);      //Maximum value of Y axis
        barChart.getAxisLeft().setAxisMinimum(0);      //Y-axis minimum value
        barChart.getAxisLeft().setLabelCount(5,false);
//        barChart.getAxisLeft().setDrawGridLines(false);

        barChart.getXAxis().setAxisMaximum(7);      //Maximum value of X axis
        barChart.getXAxis().setAxisMinimum(0);      //X-axis minimum value

        barChart.getXAxis().setLabelCount(7,false);

        final String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(weekdays));

        barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setDrawGridLines(false);
        //barChart.setFitBars(true);
        //barChart.getXAxis().setEnabled(false);

        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);      //The position of the X axis is set to down, the default is up
        barChart.getAxisRight().setEnabled(false);


        barChart.animateY(1400);






        FirebaseService.getInstance().readUserRecords(new FirebaseService.RecordDataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<Record> records, ArrayList<String> keys) throws ExecutionException, InterruptedException {
                recyclerView = (RecyclerView) view.findViewById(R.id.fragment_records_list_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                userAdapter2 = new RegularUserAdapter2(records, view.getContext());
                recyclerView.setAdapter(userAdapter2);

                recordsList = records;
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        }, uid);


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    requireActivity().finish();

                    return true;
                }
                return false;
            }
        });



        return view;
    }

    private void clearAllRecords() {
        if(recordsList != null) {
            recordsList.clear();

            if(userAdapter2 != null)
                userAdapter2.notifyDataSetChanged();
        }

        recordsList = new ArrayList<>();
    }


    @Override public void onStart()
    {
        super.onStart();
    }

    @Override public void onStop()
    {
        super.onStop();
    }
}