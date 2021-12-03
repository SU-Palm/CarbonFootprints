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
    String fragSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        btm_nav = findViewById(R.id.bottom_navigation);
        fragSelected = isNotNullOrEmpty(getIntent().getStringExtra("FRAGMENT_SELECTED")) ? getIntent().getStringExtra("FRAGMENT_SELECTED") : "Dashboard";

        getFragment(new DashboardFragment());

        btm_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard_button:
                        setAnimLeft(new DashboardFragment());
                        fragSelected = "Dashboard";
                        return true;

                    case R.id.maps_button:
                        if(fragSelected == "Dashboard") {
                            setAnimRight(new MapsFragment());
                        } else {
                            setAnimLeft(new MapsFragment());
                        }
                        fragSelected = "Maps";
                        return true;

                    case R.id.log_button:
                        if(fragSelected == "Maps") {
                            setAnimRight(new LogFragment());
                        }else if(fragSelected == "Dashboard") {
                            setAnimRight(new LogFragment());
                        }
                        else {
                            setAnimLeft(new LogFragment());
                        }
                        fragSelected = "Log";
                        return true;

                    case R.id.settings_button:
                        setAnimRight(new SettingsFragment());
                        fragSelected = "Settings";
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

    private void setAnimRight(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    private void setAnimLeft(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    private static boolean isNotNullOrEmpty(String str){
        return (str != null && !str.isEmpty());
    }

}