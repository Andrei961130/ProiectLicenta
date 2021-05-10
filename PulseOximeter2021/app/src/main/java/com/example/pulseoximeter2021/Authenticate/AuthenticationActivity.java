package com.example.pulseoximeter2021.Authenticate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.pulseoximeter2021.LinkedPacients.LinkedPacientsActivity;
import com.example.pulseoximeter2021.MainScreen.MainActivity;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Services.FirebaseService;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

public class AuthenticationActivity extends AppCompatActivity
{

    Boolean signedOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        signedOut = getIntent().getBooleanExtra("SIGN_OUT", signedOut);

        if(!signedOut)
        {
            FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        }

        if(FirebaseService.getInstance().getUser() != null && signedOut == false)
        {
//            if(!FirebaseService.getInstance().isDoctor()) {
//
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                Toast.makeText(this.getApplicationContext(), "User logged in", Toast.LENGTH_LONG).show();
//            }
//            else {
//                Intent intent = new Intent(this, LinkedPacientsActivity.class);
//                startActivity(intent);
//                Toast.makeText(this.getApplicationContext(), "Doctor logged in", Toast.LENGTH_LONG).show();
//            }

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this.getApplicationContext(), "User logged in", Toast.LENGTH_LONG).show();

        }
        else
        {
            if(signedOut)
                FirebaseService.getNewInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_auth_fragment_container, new LoginFragment())
                    .addToBackStack("LOGIN_FRAGMENT")
                    .commit();
        }

    }
}