package com.example.carbonfootprints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.carbonfootprints.Fragments.DashboardFragment;
import com.example.carbonfootprints.Fragments.LogFragment;
import com.example.carbonfootprints.Fragments.MapsFragment;
import com.example.carbonfootprints.Fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    final FragmentManager fm = getSupportFragmentManager();
    BottomNavigationView btm_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        btm_nav = findViewById(R.id.bottom_navigation);

        getFragment(new DashboardFragment());

        btm_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard_button:
                        getFragment(new DashboardFragment());
                        return true;

                    case R.id.maps_button:
                        getFragment(new MapsFragment());
                        return true;

                    case R.id.log_button:
                        getFragment(new LogFragment());
                        return true;

                    case R.id.settings_button:
                        getFragment(new SettingsFragment());
                        return true;
                }
                return false;
            }
        });
    }

    private void getFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

}