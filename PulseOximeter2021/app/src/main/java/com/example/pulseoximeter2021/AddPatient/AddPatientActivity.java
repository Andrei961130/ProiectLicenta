package com.example.pulseoximeter2021.AddPatient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;

import com.example.pulseoximeter2021.DataLayer.Models.Firebase.User;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Records.RegularUserAdapter.RegularUserAdapter2;
import com.example.pulseoximeter2021.Services.FirebaseService;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddPatientActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private AddPacientAdapter userAdapter;

    private String searchedText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        recyclerView = findViewById(R.id.activity_add_pacient_recyclerview);
        searchView = findViewById(R.id.activity_add_pacient_search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchedText = newText;
                search(searchedText);
                return true;
            }
        });

        FirebaseService.getInstance().readUserBySearchBox(new FirebaseService.UserDataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<User> users, ArrayList<String> keys) throws ExecutionException, InterruptedException {
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                userAdapter = new AddPacientAdapter(getApplicationContext(), users);
                recyclerView.setAdapter(userAdapter);
            }
        }, searchedText);
    }

    private void search(String newText) {
        FirebaseService.getInstance().readUserBySearchBox(new FirebaseService.UserDataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<User> users, ArrayList<String> keys) throws ExecutionException, InterruptedException {
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                userAdapter = new AddPacientAdapter(getApplicationContext(), users);
                recyclerView.setAdapter(userAdapter);
            }
        }, newText);
    }
}