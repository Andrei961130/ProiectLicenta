package com.example.pulseoximeter2021.FirebaseManager;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseManager
{
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
}