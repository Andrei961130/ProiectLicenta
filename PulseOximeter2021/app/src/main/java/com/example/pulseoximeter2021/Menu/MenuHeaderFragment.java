package com.example.pulseoximeter2021.Menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pulseoximeter2021.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;


public class MenuHeaderFragment extends Fragment {

    ImageView ivMenuHeader;
    public MenuHeaderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_header, container, false);
        return view;
    }
}