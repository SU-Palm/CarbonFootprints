package com.example.carbonfootprints;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_dashboard);
        //Button signOutButton = findViewById(R.id.signOutButton);
        //mAuth = FirebaseAuth.getInstance();
        //signOutButton.setOnClickListener(v -> logOut());
    }

    private void logOut() {
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.page_2:
                // Go to dashboard
                Intent intent1 = new Intent(this, Dashboard.class);
                this.startActivity(intent1);
                return true;
            case R.id.page_1:
                // Go to maps
                Intent intent2 = new Intent(this, Maps.class);
                this.startActivity(intent2);
                return true;
            case R.id.page_4:
                // Go to log
                return true;
            case R.id.page_3:
                // Go to settings
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */
}