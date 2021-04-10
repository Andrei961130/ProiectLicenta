package com.example.pulseoximeter2021.Records;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.Record;
import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.R;
//import com.example.pulseoximeter2021.Records.RegularUserAdapter.RegularUserAdapter;
import com.example.pulseoximeter2021.Records.RegularUserAdapter.RegularUserAdapter;
import com.example.pulseoximeter2021.Records.RegularUserAdapter.RegularUserAdapter2;
import com.example.pulseoximeter2021.Records.RegularUserAdapter.userAdapterTest;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.example.pulseoximeter2021.Services.FirebaseService;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A fragment representing a list of Items.
 */
public class RecordsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private RecyclerView recyclerView;
    ArrayList<Record> records;

    private RegularUserAdapter2 userAdapter2;
    public RecordsFragment() {
    }

    @SuppressWarnings("unused")
    public static RecordsFragment newInstance(int columnCount) {
        RecordsFragment fragment = new RecordsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records_list, container, false);


        FirebaseService.getInstance().readUserRecords(new FirebaseService.RecordDataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<Record> records, ArrayList<String> keys) throws ExecutionException, InterruptedException {
                recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                userAdapter2 = new RegularUserAdapter2(records);
                recyclerView.setAdapter(userAdapter2);
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
        });


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
        if(records != null) {
            records.clear();

            if(userAdapter2 != null)
                userAdapter2.notifyDataSetChanged();
        }

        records = new ArrayList<>();
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