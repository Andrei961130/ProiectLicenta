package com.example.pulseoximeter2021.Authenticate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pulseoximeter2021.MainScreen.MainActivity;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Services.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.royrodriguez.transitionbutton.TransitionButton;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etEmail;
    private EditText etPassword;
    private TransitionButton btnSignIn;
    private TextView tvCreateAccount;

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
        tvCreateAccount = view.findViewById(R.id.fragment_login_tv_create_account);

        btnSignIn.setOnClickListener(this::loginButtonClick);
        tvCreateAccount.setOnClickListener(this::createAccountClick);

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
        hideSoftKeyboard(requireActivity());
        btnSignIn.startAnimation();

        String emailStr = etEmail.getText().toString();
        String passwordStr = etPassword.getText().toString();

        if(emailStr.isEmpty())
        {
            btnSignIn.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
        }
        else if(passwordStr.isEmpty())
        {
            btnSignIn.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches())
        {
            btnSignIn.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
        }
        else if(passwordStr.length() < 6)
        {
            btnSignIn.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
            etPassword.setError("The password must be at least 6 characters long");
            etPassword.requestFocus();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(getActivity(), task -> {
                if (!task.isSuccessful()) {
//                    Toast.makeText(requireActivity().getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    btnSignIn.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);

                    try {
                        throw task.getException();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        etEmail.setError("Invalid credentials");
                        etEmail.requestFocus();
                        etPassword.setText("");
                    } catch(Exception e) {
//                        Toast.makeText(requireActivity().getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    FirebaseService.getNewInstance();
                    requireActivity().finish();

                    btnSignIn.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                        @Override
                        public void onAnimationStopEnd() {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
//                            Toast.makeText(requireActivity().getApplicationContext(), "User logged in", Toast.LENGTH_LONG).show();
                        }
                    });

//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    startActivity(intent);
//                    Toast.makeText(requireActivity().getApplicationContext(), "User logged in", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void removeLoginFragment() {
        LoginFragment fragment = (LoginFragment) getFragmentManager().findFragmentById(R.id.activity_auth_fragment_container);
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                inputManager.hideSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}