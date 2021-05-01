package com.example.pulseoximeter2021.LinkedPacients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Services.FirebaseService;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LinkedPacientsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinkedPacientsAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_pacients);

        FirebaseService.getInstance().readUsersByDoctor(new FirebaseService.UserDataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<User> users, ArrayList<String> keys) throws ExecutionException, InterruptedException {
                recyclerView = (RecyclerView) findViewById(R.id.activity_add_pacient_recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                userAdapter = new LinkedPacientsAdapter(getBaseContext(), users);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void DataIsInserted() {

            }
        });
    }
}