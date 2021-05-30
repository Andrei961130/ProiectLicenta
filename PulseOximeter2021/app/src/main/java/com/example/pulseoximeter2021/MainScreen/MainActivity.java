package com.example.pulseoximeter2021.MainScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pulseoximeter2021.Bluetooth.BluetoothHelper;
import com.example.pulseoximeter2021.MainScreen.ViewPager.ViewPagerAdapter;
import com.example.pulseoximeter2021.Menu.MenuFragment;
import com.example.pulseoximeter2021.R;
import com.example.pulseoximeter2021.Services.FirebaseService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

public class MainActivity extends AppCompatActivity implements StartingFragment.OnBluetoothConnectionChangedListener {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FlowingDrawer flowingDrawer;

    private TextView toolbarTitle;
    private ImageView ivBluetooth;
    private ImageView ivMenuBars;

    private CoordinatorLayout parentView;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flowingDrawer = findViewById(R.id.activity_main_drawer_layout);
        flowingDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);

        toolbarTitle = findViewById(R.id.activity_main_drawer_toolbar_title);
        ivBluetooth = findViewById(R.id.activity_main_drawer_toolbar_bluetooth_icon);
        ivMenuBars = findViewById(R.id.activity_main_drawer_toolbar_menu_icon);

        viewPager2 = findViewById(R.id.activity_main_view_pager);
        tabLayout = findViewById(R.id.activity_main_tab_layout);
        parentView = findViewById(R.id.activity_main_coordinator_layout);

        setupToolbar();
        setBluetoothIvColorRed();
        setMenuBarsListener();
        setupMenu();
        displayMainFragment();
        setupViewPager();

        ivBluetooth.setOnClickListener(v -> {

            BluetoothHelper bluetoothHelper = BluetoothHelper.getInstance();

            if(bluetoothHelper.isConnected())
                setBluetoothIvColorGreen();
            else
                bluetoothHelper.Connect("HC-05");
        });
    }

    private void setupViewPager() {
        viewPager2.setAdapter(new ViewPagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //set something for tab
//                tab.setText("OBJECT " + (position + 1));
//                tab.setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_LABELED);
                tab.setIcon(R.drawable.ic_tab_indicator_selected);
            }
        }).attach();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                switch (position)
                {
                    case 0:
                        FirebaseService.getInstance().setDuration(10);
                        break;
                    case 1:
                        FirebaseService.getInstance().setDuration(15);
                        break;
                    case 2:
                        FirebaseService.getInstance().setDuration(20);
                        break;
                    default:
                        FirebaseService.getInstance().setDuration(10);
                        break;
                }
            }
        });
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

        if(FirebaseService.getInstance().getCurrentUser() != null)
            toolbarTitle.setText(FirebaseService.getInstance().getCurrentUser().getDisplayName().split(" ")[0]);
        else
            toolbarTitle.setText("");

//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.postDelayed(() -> {
//            try {
//                toolbarTitle.setText(FirebaseService.getInstance().getUserDetails().getFirstName());
//            }
//            catch (Exception e)
//            {
//                Handler handler1 = new Handler(Looper.getMainLooper());
//                handler1.postDelayed(() -> {
//                    try {
//                        toolbarTitle.setText(FirebaseService.getInstance().getUserDetails().getFirstName());
//                    }
//                    catch (Exception e1)
//                    {
//
//                    }
//
//                }, 2000);
//            }
//
//        }, 1000);

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

    private void displayMainFragment() {

        getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.activity_main_fragment_container, new StartingFragment())
        .addToBackStack("STARTING_FRAGMENT")
        .commit();
    }

    @Override
    public void onBackPressed() {
        if (flowingDrawer.isMenuVisible()) {
            flowingDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void changeBluetoothIcon(Boolean connected) {
        if (connected) {
            setBluetoothIvColorGreen();
        } else {
            setBluetoothIvColorRed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flowingDrawer.isMenuVisible()) {
            flowingDrawer.closeMenu();
        }
    }
}