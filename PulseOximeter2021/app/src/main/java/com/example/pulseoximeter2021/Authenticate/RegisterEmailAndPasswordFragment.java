package com.example.pulseoximeter2021.Authenticate;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pulseoximeter2021.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class RegisterEmailAndPasswordFragment extends Fragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPassword2;
    private Button btnContinue;

    public RegisterEmailAndPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_email_and_password, container, false);

        etEmail = view.findViewById(R.id.fragment_register_et_email);
        etPassword = view.findViewById(R.id.fragment_register_et_password);
        etPassword2 = view.findViewById(R.id.fragment_register_et_password2);
        btnContinue = view.findViewById(R.id.fragment_register_email_and_password_btn_continue);

//        GradientDrawable drawable = new GradientDrawable();
//        drawable.setColor(Color.BLACK);
//        drawable.setShape(GradientDrawable.RECTANGLE);
//        drawable.setStroke(3, Color.YELLOW);
//        drawable.setCornerRadius(8);
//        btnContinue.setBackgroundDrawable(drawable);
//        btnContinue.setBackgroundResource(Drawable);


        btnContinue.setOnClickListener(this::checkFieldsAndContinue);

        return view;
    }

    private void checkFieldsAndContinue(View view)
    {
        String emailStr = etEmail.getText().toString();
        String passwordStr = etPassword.getText().toString();
        String password2Str = etPassword2.getText().toString();

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
        else if(password2Str.isEmpty())
        {
            etPassword2.setError("Please type your password again");
            etPassword2.requestFocus();
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
        else if(!passwordStr.equals(password2Str))
        {
            etPassword2.setError("The password does not match");
            etPassword2.requestFocus();
            etPassword2.setText("");
        }
        else
        {
            firebaseAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                if (!task.isSuccessful()) {

                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        etPassword.setError("This password is too weak");
                        etPassword.requestFocus();
                        etPassword.setText("");
                        etPassword2.setText("");
                    } catch(FirebaseAuthUserCollisionException e) {
                        etEmail.setError("An user with this email already exists");
                        etEmail.requestFocus();
                        etEmail.setText("");
                        etPassword.setText("");
                        etPassword2.setText("");
                    } catch(Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "User created", Toast.LENGTH_LONG).show();

                    Bundle bundle = new Bundle();
                    bundle.putString("email", emailStr);

                    RegisterAllOtherFragment fragment = new RegisterAllOtherFragment();
                    fragment.setArguments(bundle);

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.activity_auth_fragment_container, fragment)
                            .addToBackStack("REGISTER_ALL_OTHER_FRAGMENT")
                            .commit();
                }
            });





        }
    }
}