package com.example.pulseoximeter2021.Records;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pulseoximeter2021.DataLayer.GroupBarValue;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class RecordsFragment extends Fragment {

    LinearLayout maskLayout;
    TextView tvMaskText;

    private User user = null;
    private String uid = null;

    private RecyclerView recyclerView;
    ArrayList<Record> recordsList;

    private RegularUserAdapter2 userAdapter2;

    private BarChart barChart;
    private ArrayList<BarEntry> greenRecords;
    private ArrayList<BarEntry> orangeRecords;
    private ArrayList<BarEntry> redRecords;

    String[] labelValues = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    private ArrayList<Calendar> labelValuesCalendarList;
    private HashMap<Calendar, GroupBarValue> graphRecordsByDay;

    private Integer maxBarValue = 0;

    private Boolean hasRecords = false;

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

        seedLabels();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records_list2, container, false);

        barChart = view.findViewById(R.id.fragment_records_list_bar_chart);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_records_list_recycler_view);
        maskLayout = view.findViewById(R.id.fragment_records_list2_no_record_linear_layout);
        tvMaskText = view.findViewById(R.id.fragment_records_list2_no_record_text);

        barChart.setVisibility(View.INVISIBLE);

        FirebaseService.getInstance().readUserRecords(new FirebaseService.RecordDataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<Record> records, ArrayList<String> keys) throws ExecutionException, InterruptedException {

                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                userAdapter2 = new RegularUserAdapter2(records, view.getContext());
                recyclerView.setAdapter(userAdapter2);

                if(!records.isEmpty())
                    hasRecords=true;

                recordsList = (ArrayList<Record>) records.clone();

                initializeAndFeedGraph();
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

    private void initializeAndFeedGraph() {

        pickRecordsForGraph();

        greenRecords = new ArrayList<>(7);
        orangeRecords = new ArrayList<>(7);
        redRecords = new ArrayList<>(7);

        for (Integer index = 0; index < labelValuesCalendarList.size(); index++)
        {
            Calendar currentLabelCalendar = labelValuesCalendarList.get(index);
            GroupBarValue currentGroupBarValue = graphRecordsByDay.get(currentLabelCalendar);

            Integer currentGreenValue = currentGroupBarValue.getGreen();
            Integer currentOrangeValue = currentGroupBarValue.getOrange();
            Integer currentRedValue = currentGroupBarValue.getRed();

             greenRecords.add(new BarEntry(index + 1,currentGreenValue));
            orangeRecords.add(new BarEntry(index + 1,currentOrangeValue));
            redRecords.add(new BarEntry(index + 1,currentRedValue));


            if(currentGreenValue > maxBarValue)
                maxBarValue = currentGreenValue;

            if(currentOrangeValue > maxBarValue)
                maxBarValue = currentOrangeValue;

            if(currentRedValue > maxBarValue)
                maxBarValue = currentRedValue;
        }


        if(maxBarValue == 0 && !hasRecords)
        {
            tvMaskText.setText("You have no records yet");
            return;
        }
        else
        {
            maskLayout.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
        }

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
        barData.setDrawValues(false);

        barChart.setData(barData);

        barData.setBarWidth(0.2f);
        barData.groupBars(0f,0.4f,0);

        barChart.setBackgroundColor(Color.TRANSPARENT);

        barChart.getAxisLeft().setAxisMaximum(maxBarValue + 1);      //Maximum value of Y axis
        barChart.getAxisLeft().setAxisMinimum(0);      //Y-axis minimum value
        barChart.getAxisLeft().setLabelCount(maxBarValue + 1,false);
//        barChart.getAxisLeft().setDrawGridLines(false);

        barChart.getXAxis().setAxisMaximum(7);      //Maximum value of X axis
        barChart.getXAxis().setAxisMinimum(0);      //X-axis minimum value

        barChart.getXAxis().setLabelCount(7,false);


        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labelValues));

        barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setDrawGridLines(false);
        //barChart.setFitBars(true);
        //barChart.getXAxis().setEnabled(false);

        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);      //The position of the X axis is set to down, the default is up
        barChart.getAxisRight().setEnabled(false);


        barChart.animateY(1400);




        Integer a = 0;
        a++;

    }

    private void pickRecordsForGraph(){
        for (Record record :
                recordsList) {
            Calendar currentRecordDate = Calendar.getInstance();
            currentRecordDate.setTime(record.getDateAndTimeAsDate());

            for (Calendar currentCalendar :
                    labelValuesCalendarList) {
                if (isSameDateTime(currentCalendar, currentRecordDate)) {
                    GroupBarValue currentGroupValue = graphRecordsByDay.get(currentCalendar);

                    if(record.getAverageBpmValue() <= 70)
                        currentGroupValue.incrementGreen();
                    else if(record.getAverageBpmValue() > 70 && record.getAverageBpmValue() <= 90)
                        currentGroupValue.incrementOrange();
                    else currentGroupValue.incrementRed();

                    graphRecordsByDay.put(currentCalendar, currentGroupValue);
                }
            }
        }
    }

    public boolean isSameDateTime(Calendar cal1, Calendar cal2) {
        // compare if is the same YEAR, DAY, HOUR
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    private void seedLabels()
    {
        labelValuesCalendarList = new ArrayList<>();
        graphRecordsByDay = new HashMap<>();

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -7);

        for(int i = 0; i< 7; i++){
            cal.add(Calendar.DAY_OF_YEAR, 1);
            //System.out.println(sdf.format(cal.getTime()));
            labelValues[i] = sdf.format(cal.getTime());

            labelValuesCalendarList.add((Calendar) cal.clone());
            graphRecordsByDay.put((Calendar) cal.clone(), new GroupBarValue());
        }
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