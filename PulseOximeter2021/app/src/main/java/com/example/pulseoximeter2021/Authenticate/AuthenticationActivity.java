package com.example.pulseoximeter2021.Authenticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pulseoximeter2021.MainScreen.MainActivity;
import com.example.pulseoximeter2021.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.activity_auth_fragment_container, new LoginFragment())
//                .addToBackStack("LOGIN_FRAGMENT")
//                .commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}