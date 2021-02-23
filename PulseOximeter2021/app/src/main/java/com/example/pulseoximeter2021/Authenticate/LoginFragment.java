package com.example.pulseoximeter2021.Authenticate;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pulseoximeter2021.MainScreen.MainActivity;
import com.example.pulseoximeter2021.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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

        return view;
    }

    private void createAccountClick(View view)
    {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_auth_fragment_container, new RegisterEmailAndPasswordFragment())
                .addToBackStack("REGISTER_EMAIL_FRAGMENT")
                .commit();
    }

    private void loginButtonClick(View view)
    {
        String emailStr = etEmail.getText().toString();
        String passwordStr = etPassword.getText().toString();

        if(emailStr.isEmpty())
        {
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
        }
        else if(passwordStr.isEmpty())
        {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches())
        {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
        }
        else if(passwordStr.length() < 6)
        {
            etPassword.setError("The password must be at least 6 characters long");
            etPassword.requestFocus();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(getActivity(), task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    try {
                        throw task.getException();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        etEmail.setError("Invalid credentials");
                        etEmail.requestFocus();
                        etPassword.setText("");
                    } catch(Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getActivity().getApplicationContext(), "User logged in", Toast.LENGTH_LONG).show();

                    //removeLoginFragment();
                }
            });
        }
    }

    private void removeLoginFragment() {
        LoginFragment fragment = (LoginFragment) getFragmentManager().findFragmentById(R.id.activity_auth_fragment_container);
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }
}