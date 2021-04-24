package com.example.pulseoximeter2021.Authenticate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.pulseoximeter2021.MainScreen.MainActivity;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Services.FirebaseService;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class AuthenticationActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        FirebaseUser user = FirebaseService.getInstance().getUser();

        if(user != null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this.getApplicationContext(), "User logged in", Toast.LENGTH_LONG).show();
        }
        else
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_auth_fragment_container, new LoginFragment())
                    .addToBackStack("LOGIN_FRAGMENT")
                    .commit();
        }



//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }
}