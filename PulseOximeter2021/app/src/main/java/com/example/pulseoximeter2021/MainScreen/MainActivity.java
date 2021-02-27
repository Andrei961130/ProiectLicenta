package com.example.pulseoximeter2021.MainScreen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pulseoximeter2021.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FlowingDrawer flowingDrawer;
    private TextView toolbarTitle;
    private ImageView ivBluetooth;
    private ImageView ivMenuBars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flowingDrawer = findViewById(R.id.activity_main_drawer_layout);
        flowingDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);

        toolbarTitle = findViewById(R.id.activity_main_drawer_toolbar_title);
        ivBluetooth = findViewById(R.id.activity_main_drawer_toolbar_bluetooth_icon);
        ivMenuBars = findViewById(R.id.activity_main_drawer_toolbar_menu_icon);

        setupToolbar();
        setBluetoothIvColorRed();
        setMenuBarsListener();
    }

    private void setMenuBarsListener() {
        ivMenuBars.setOnClickListener(v -> flowingDrawer.openMenu(true));
    }

    private void setBluetoothIvColorRed() {
        Resources res = getApplicationContext().getResources();
        int newColor = res.getColor(R.color.design_default_color_error);
        ivBluetooth.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
    }

    private void setBluetoothIvColorGreen() {
        Resources res = getApplicationContext().getResources();
        int newColor = res.getColor(R.color.green);
        ivBluetooth.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
    }

    protected void setupToolbar() {
        String userName = firebaseAuth.getCurrentUser().getDisplayName().split(" ")[0];
        toolbarTitle.setText(userName.isEmpty()?"HAALIII":userName);
    }
}