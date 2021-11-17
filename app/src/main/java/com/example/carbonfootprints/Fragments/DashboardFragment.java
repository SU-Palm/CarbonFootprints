package com.example.carbonfootprints.Fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carbonfootprints.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment implements SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    private FirebaseAuth mAuth;

    // System sensor manager instance.
    private SensorManager mSensorManager;

    // sensor, as retrieved from the sensor manager.
    private Sensor mSensorAccelerometer;

    // TextView to display current sensor values.
    private TextView mTextSensorAccelerometer;

    //sensor, as retrieved from sensor manager.
    private Sensor mSensorPedometer;

    //TextView to display current pedometer value.
    private TextView mTextSensorPedometer;


    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mTextSensorAccelerometer = (TextView) view.findViewById(R.id.label_accelerometer);
        mTextSensorPedometer = (TextView) view.findViewById(R.id.label_pedometer);

        // Get an instance of the sensor manager.
        mSensorManager = (SensorManager) getActivity().getSystemService(
                Context.SENSOR_SERVICE);

        // Get accelerometer sensor from the sensor manager.
        // The getDefaultSensor() method returns null if the sensor
        // is not available on the device.
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorPedometer = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // Get the error message from string resources.
        String sensor_error = getResources().getString(R.string.error_no_sensor);

        // If mSensorAccelerometer is null, the sensor
        // is not available in the device.  Set the text to the error message
        if (mSensorAccelerometer == null) { mTextSensorAccelerometer.setText(sensor_error); }
        if (mSensorPedometer == null) { mTextSensorPedometer.setText(sensor_error); }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Listener for the sensor is registered in this callback and
        // can be unregistered in onPause().
        // Check to ensure sensor is available before registering listener.
        // listener is registered with a "normal" amount of delay
        if (mSensorAccelerometer != null) {
            mSensorManager.registerListener(this, mSensorAccelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mSensorPedometer != null) {
            mSensorManager.registerListener(this, mSensorPedometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onStop() {
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
        float stepCount = 0;

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                // Set the accelerometer sensor text view to the light sensor
                // string from the resources, with the placeholder filled in.
                mTextSensorAccelerometer.setText(getResources().getString(
                        R.string.label_accelerometer, currentValue));
                break;
            case Sensor.TYPE_STEP_COUNTER:
                mTextSensorPedometer.setText(getResources().getString(
                        R.string.label_pedometer, currentValue));
                break;
            default:
                // do nothing
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}