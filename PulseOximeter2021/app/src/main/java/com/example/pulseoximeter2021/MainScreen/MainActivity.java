package com.example.pulseoximeter2021.MainScreen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

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

import com.example.pulseoximeter2021.Menu.MenuFragment;
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
        setupMenu();
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
        String userName = firebaseAuth.getCurrentUser().getDisplayName();
        toolbarTitle.setText(userName == null?
                "HAALIII":userName.isEmpty()?
                "HAALII": userName);
    }

    private void setupMenu() {
        FragmentManager fm = getSupportFragmentManager();
        MenuFragment menuFragment = (MenuFragment) fm.findFragmentById(R.id.activity_main_drawer_menu_container);
        if (menuFragment == null) {
            menuFragment = new MenuFragment();
            fm.beginTransaction().add(R.id.activity_main_drawer_menu_container, menuFragment).commit();
        }

//        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
//            @Override
//            public void onDrawerStateChange(int oldState, int newState) {
//                if (newState == ElasticDrawer.STATE_CLOSED) {
//                    Log.i("MainActivity", "Drawer STATE_CLOSED");
//                }
//            }
//
//            @Override
//            public void onDrawerSlide(float openRatio, int offsetPixels) {
//                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
//            }
//        });
    }
}