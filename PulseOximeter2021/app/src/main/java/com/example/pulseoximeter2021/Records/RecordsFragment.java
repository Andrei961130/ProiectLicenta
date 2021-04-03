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

/**
 * A fragment representing a list of Items.
 */
public class RecordsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private RecyclerView recyclerView;
    private RegularUserAdapter userAdapter;
    private DatabaseReference databaseReference;

    RecyclerView recyclerView2;
    DatabaseReference  dbRef;
    ArrayList<Record> records;

    private RegularUserAdapter2 userAdapter2;
    private userAdapterTest userAdapterTest;

    RecyclerView recyclerViewBAAAA;


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

//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setLogLevel(Logger.Level.DEBUG);

//        firebaseDatabase.goOffline();
//        firebaseDatabase.goOnline();

//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Records").child(uid);
//        databaseReference = firebaseDatabase.getReference("/Users");
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot keyNode : dataSnapshot.getChildren())
//                {
//                    User value = keyNode.getValue(User.class);
//
//                    ArrayList<User> usersList = new ArrayList<User>();
//                    usersList.add(value);
//                    userAdapterTest = new userAdapterTest(usersList);
//                    recyclerViewBAAAA.setAdapter(userAdapterTest);
//
//                    Log.d("TAG", "Value is: " + value.toString());
//                    Toast.makeText(getActivity().getApplicationContext(), value.toString(), Toast.LENGTH_LONG).show();
////                    caca.add(value);
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("TAG", "Failed to read value.", error.toException());
//                Toast.makeText(getActivity().getApplicationContext(), "Am crapat bagamias pla", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records_list, container, false);

        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            recyclerView.setLayoutManager(new LinearLayoutManager(context));
//
//            recyclerView.setAdapter(new MyRecordsRecyclerViewAdapter(DummyContent.ITEMS));
//        }


        //recyclerview FIREBASE


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String uid = firebaseUser.getUid();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


        databaseReference = firebaseDatabase.getReference().child("Records").child(uid);

         Query query = databaseReference;

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //ClearAll
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //Record Record2 = dataSnapshot.getValue(Record.class);

                    Record record = new Record();

                    record.setBpmValues((List<Integer>) dataSnapshot.child("bpmValues").getValue());
//                    try {
//                        record.setDateAndTime(new SimpleDateFormat("yyyy.MM.dd hh:mm:ss").parse(dataSnapshot.child("dateAndTimeAsString").getValue().toString()));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }

                    //record.setDateAndTime(dataSnapshot.child("dateAndTimeAsString").getValue().toString());
                    record.setFullName( dataSnapshot.child("fullName").getValue().toString());
                    record.setIrValues((List<Integer>) dataSnapshot.child("irValues").getValue());
                    //record.setLength((Integer) dataSnapshot.child("lenght").getValue());
                    record.setMessage( dataSnapshot.child("message").getValue().toString());
                    //record.setOxygen((Integer) dataSnapshot.child("oxygen").getValue());
                    //record.setTemperature((Double) dataSnapshot.child("temperature").getValue());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String a = "";
            }
        });

        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        FirebaseRecyclerOptions<Record> options
                = new FirebaseRecyclerOptions.Builder<Record>()
                .setQuery(query, Record.class)
                .build();

        userAdapter = new RegularUserAdapter(options);
        recyclerView.setAdapter(userAdapter);


/*    ArrayList<Record> recordArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Records");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recordArrayList.clear();
                for(DataSnapshot snapshot2 : snapshot.getChildren())
                {
                    recordArrayList.add(snapshot2.getValue(Record.class));
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error getting data", error.toException());
            }
        });*/



//        recyclerView2 = (RecyclerView) view;
//        recyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        recyclerView2.setHasFixedSize(true);
//
//
//        dbRef = FirebaseDatabase.getInstance().getReference("Records");
//
//        clearAllRecords();
//        getRecordsFromFirebase();
//
//
//        records.add(Record.GenerateRandom());
//        userAdapter2 = new RegularUserAdapter2(records);
//        recyclerView2.setAdapter(userAdapter2);






/*        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d("firebase", "connected");
                } else {
                    Log.d("firebase", "not connected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("firebase", "Listener was cancelled");
            }
        });*/


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

    private void getRecordsFromFirebase() {

//        Query query = dbRef.child("Records");
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //ClearAll
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    Record record = new Record();
//
//                    record.setBpmValues((List<Integer>) dataSnapshot.child("bpmValues").getValue());
//                    try {
//                        record.setDateAndTime(new SimpleDateFormat("yyyy.MM.dd hh:mm:ss").parse(dataSnapshot.child("dateAndTimeAsString").getValue().toString()));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    record.setFullName( dataSnapshot.child("fullName").getValue().toString());
//                    record.setIrValues((List<Integer>) dataSnapshot.child("irValues").getValue());
//                    record.setLength((Integer) dataSnapshot.child("lenght").getValue());
//                    record.setMessage( dataSnapshot.child("lenght").getValue().toString());
//                    record.setOxygen((Integer) dataSnapshot.child("oxygen").getValue());
//                    record.setTemperature((Double) dataSnapshot.child("temperature").getValue());
//
//                    records.add(record);
//                }
//
//                userAdapter2 = new RegularUserAdapter2(records);
//                recyclerView2.setAdapter(userAdapter2);
//                recyclerView2.notifyAll();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                String a = "";
//            }
//        });
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
        userAdapter.startListening();
    }

    @Override public void onStop()
    {
        super.onStop();
        userAdapter.stopListening();
    }
}