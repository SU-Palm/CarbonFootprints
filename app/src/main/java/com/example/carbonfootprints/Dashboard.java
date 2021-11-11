package com.example.carbonfootprints;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity implements SensorEventListener {
    private FirebaseAuth mAuth;

    // System sensor manager instance.
    private SensorManager mSensorManager;

    // sensor, as retrieved from the sensor manager.
    private Sensor mSensorAccelerometer;

    // TextView to display current sensor values.
    private TextView mTextSensorAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_dashboard);
        //Button signOutButton = findViewById(R.id.signOutButton);
        //mAuth = FirebaseAuth.getInstance();
        //signOutButton.setOnClickListener(v -> logOut());


        // Initialize view variable.
        mTextSensorAccelerometer = (TextView) findViewById(R.id.label_accelerometer);

        // Get an instance of the sensor manager.
        mSensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);

        // Get accelerometer sensor from the sensor manager.
        // The getDefaultSensor() method returns null if the sensor
        // is not available on the device.
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Get the error message from string resources.
        String sensor_error = getResources().getString(R.string.error_no_sensor);

        // If mSensorAccelerometer is null, the sensor
        // is not available in the device.  Set the text to the error message
        if (mSensorAccelerometer == null) { mTextSensorAccelerometer.setText(sensor_error); }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Listener for the sensor is registered in this callback and
        // can be unregistered in onPause().
        // Check to ensure sensor is available before registering listener.
        // listener is registered with a "normal" amount of delay
        if (mSensorAccelerometer != null) {
            mSensorManager.registerListener(this, mSensorAccelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unregister sensor listener in this callback so they don't
        // continue to use resources when the app is paused.
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // The sensor type (as defined in the Sensor class).
        int sensorType = sensorEvent.sensor.getType();

        // The new data value of the sensor. accelerometer sensor
        // reports one value at a time, which is always the first
        // element in the values array.
        float currentValue = sensorEvent.values[0];

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                // Set the accelerometer sensor text view to the light sensor
                // string from the resources, with the placeholder filled in.
                mTextSensorAccelerometer.setText(getResources().getString(
                        R.string.label_accelerometer, currentValue));
                break;
            default:
                // do nothing
        }
    }

     //Abstract method in SensorEventListener.  It must be implemented, but is
     //unused in this app.
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
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