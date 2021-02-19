package com.example.pulseoximeter2021.Authenticate;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pulseoximeter2021.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignIn;
    private Button btnCreateAccount;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etEmail = view.findViewById(R.id.fragment_login_et_email);
        etPassword = view.findViewById(R.id.fragment_login_et_password);
        btnSignIn = view.findViewById(R.id.fragment_login_btn_sign_in);
        btnCreateAccount = view.findViewById(R.id.fragment_login_btn_create_account);

        btnSignIn.setOnClickListener(this::loginButtonClick);

        btnCreateAccount.setOnClickListener(this::createAccountClick);

        Toast.makeText(getActivity().getApplicationContext(), "User logged in", Toast.LENGTH_LONG).show();

        return view;
    }

    private void createAccountClick(View view)
    {

    }

    private void loginButtonClick(View view)
    {
        String emailStr = etEmail.getText().toString();
        String passwordStr = etPassword.getText().toString();

        if(emailStr.isEmpty())
        {
            etEmail.setError("Please enter your username");
            etEmail.requestFocus();
        }
        else if(passwordStr.isEmpty())
        {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
        }
        else if(!(emailStr.isEmpty() && passwordStr.isEmpty()))
        {
            mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(getActivity(), task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "User logged in", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}