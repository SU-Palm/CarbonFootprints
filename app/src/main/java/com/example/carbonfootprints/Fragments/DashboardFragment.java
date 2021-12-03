package com.example.carbonfootprints.Fragments;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

import java.lang.Math;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carbonfootprints.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private DatabaseReference mDatabase;

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

    //TextView distances saved
    private TextView mTextMilesSaved;

    //longs for database values pulled
    private long accValue;
    private long pedValue;

    //floats for values to push to database
    private float updatedAccVal;
    private float updatedPedVal;
    private double currentMiles;

    public TextView mUserNameOne;
    public TextView mUserNameTwo;
    public TextView mUserNameThree;

    public TextView mUserNameOneMiles;
    public TextView mUserNameTwoMiles;
    public TextView mUserNameThreeMiles;

    public TextView milesSaved;
    public TextView lastTrip;


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
        mTextMilesSaved = (TextView) view.findViewById(R.id.distanceSavedText);

        // Get an instance of the sensor manager.
        mSensorManager = (SensorManager) getActivity().getSystemService(
                Context.SENSOR_SERVICE);

        // Get accelerometer sensor from the sensor manager.
        // The getDefaultSensor() method returns null if the sensor
        // is not available on the device.
        mSensorAccelerometer = mSensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
        mSensorPedometer = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // Get the error message from string resources.
        String sensor_error = getResources().getString(R.string.error_no_sensor);

        // If mSensorAccelerometer is null, the sensor
        // is not available in the device.  Set the text to the error message
        if (mSensorAccelerometer == null) { mTextSensorAccelerometer.setText(sensor_error); }
        if (mSensorPedometer == null) { mTextSensorPedometer.setText(sensor_error); }

        mUserNameOne = view.findViewById(R.id.numberOneUserName);
        mUserNameTwo = view.findViewById(R.id.numberTwoUserName);
        mUserNameThree = view.findViewById(R.id.numberThreeUserName);

        mUserNameOneMiles = view.findViewById(R.id.numberOneUserMiles);
        mUserNameTwoMiles = view.findViewById(R.id.numberTwoUserMiles);
        mUserNameThreeMiles = view.findViewById(R.id.numberThreeUserMiles);

        milesSaved = view.findViewById(R.id.milesSaved);
        lastTrip = view.findViewById(R.id.lastTrip);

        setLeaderboard();
        setUserData();

        // createTrips();

        return view;
    }

    private void setLeaderboard() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //String uId = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("Firebase Users", String.valueOf(task.getResult().getValue()));
                    Map<String, Map<String, Object>> users = (Map<String, Map<String, Object>>) task.getResult().getValue();
                    System.out.println("Printing NonSorted Users:" + users);
                    System.out.println("Printing Sorted Users:" + entriesSortedByValues(users));
                    mUserNameOne.setText(entriesSortedByValues(users).get(0).getValue().get("email").toString().substring(0, 8).concat("****"));
                    mUserNameTwo.setText(entriesSortedByValues(users).get(1).getValue().get("email").toString().substring(0, 8).concat("****"));
                    mUserNameThree.setText(entriesSortedByValues(users).get(2).getValue().get("email").toString().substring(0, 8).concat("****"));

                    mUserNameOneMiles.setText(entriesSortedByValues(users).get(0).getValue().get("milesSaved").toString().concat("   Miles"));
                    mUserNameTwoMiles.setText(entriesSortedByValues(users).get(1).getValue().get("milesSaved").toString().concat("   Miles"));
                    mUserNameThreeMiles.setText(entriesSortedByValues(users).get(2).getValue().get("milesSaved").toString().concat("   Miles"));
                }
            }
        });
    }

    private void setUserData() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uId = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        mDatabase.child(uId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("Firebase Users", String.valueOf(task.getResult().getValue()));
                    Map<String, Object> user = (Map<String, Object>) task.getResult().getValue();
                    milesSaved.setText(user.get("milesSaved").toString());
                    lastTrip.setText(user.get("lastTrip").toString());
                }
            }
        });
    }
    private void getDatabaseVals() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uId = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        mDatabase.child(uId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("getDatabaseVals", String.valueOf(task.getResult().getValue()));
                    Map<String, Object> user = (Map<String, Object>) task.getResult().getValue();
                    pedValue = (long) user.get("distanceWalked");
                    accValue = (long) user.get("distanceDrove");
                }
            }
        });
    }
    private void updateDatabaseVals() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uId = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("UserData").child(uId);
        mDatabase.child("milesSaved").setValue( (long) currentMiles);
        mDatabase.child("distanceWalked").setValue( (long) updatedPedVal);
        mDatabase.child("distanceDrove").setValue( (long) updatedAccVal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("update acc succesful", uId);

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Log.d("update FAILED", "notOK");

                    }
                });

    }

    private void createTrips() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uId = user.getUid();

        Map<String, Map<String, Object>> trips = new HashMap<String, Map<String, Object>>();
        Map<String, Object> trip1 = new HashMap<String, Object>();
        trip1.put("date", "05 JAN");
        trip1.put("distance", "5 Miles");

        Map<String, Object> trip2 = new HashMap<String, Object>();
        trip2.put("date", "07 FEB");
        trip2.put("distance", "15 Miles");

        Map<String, Object> trip3 = new HashMap<String, Object>();
        trip3.put("date", "13 MAR");
        trip3.put("distance", "12 Miles");

        Map<String, Object> trip4 = new HashMap<String, Object>();
        trip4.put("date", "23 APR");
        trip4.put("distance", "7 Miles");

        Map<String, Object> trip5 = new HashMap<String, Object>();
        trip5.put("date", "04 MAY");
        trip5.put("distance", "11 Miles");

        Map<String, Object> trip6 = new HashMap<String, Object>();
        trip6.put("date", "01 JUNE");
        trip6.put("distance", "6 Miles");

        Map<String, Object> trip7 = new HashMap<String, Object>();
        trip7.put("date", "13 JULY");
        trip7.put("distance", "2 Miles");

        trips.put("Trip 1", trip1);
        trips.put("Trip 2", trip2);
        trips.put("Trip 3", trip3);
        trips.put("Trip 4", trip4);
        trips.put("Trip 5", trip5);
        trips.put("Trip 6", trip6);
        trips.put("Trip 7", trip7);
        System.out.println("Printing trips: " + trips);

        mDatabase.child(uId).child("Trips").setValue(trips);
    }

    public static <K, V extends Comparable<? super V>> List<Map.Entry<String, Map<String, Object>>> entriesSortedByValues(Map<String, Map<String, Object>> map) {

        List<Map.Entry<String, Map<String, Object>>> sortedEntries = new ArrayList<Map.Entry<String, Map<String, Object>>>(map.entrySet());

        Collections.sort(sortedEntries, new Comparator<Map.Entry<String, Map<String, Object>>>() {
            @Override
            public int compare(Map.Entry<String, Map<String, Object>> t1, Map.Entry<String, Map<String, Object>> t2) {
                Map<String, Object> firstMap = t1.getValue();
                Map<String, Object> secondMap = t2.getValue();
                long firstMiles = (long) firstMap.get("milesSaved");
                long secondMiles = (long) secondMap.get("milesSaved");

                if(firstMiles > secondMiles) {
                    return -1;
                } else if(firstMiles < secondMiles) {
                    return 1;
                } else {
                    return 0;
                }
            }

        });

        return sortedEntries;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Listener for the sensor is registered in this callback and
        // can be unregistered in onPause().
        // Check to ensure sensor is available before registering listener.
        // listener is registered with a "normal" amount of delay
        getDatabaseVals();
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
        updateDatabaseVals();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // The sensor type (as defined in the Sensor class).
        int sensorType = sensorEvent.sensor.getType();
        long baseVal;
        // The new data value of the sensor. accelerometer sensor
        // reports one value at a time, which is always the first
        // element in the values array.
        if (sensorType == TYPE_ACCELEROMETER) {
            baseVal = accValue;
            updatedAccVal = baseVal;
        }
        else {
            baseVal = pedValue;
            updatedPedVal = baseVal;
        }
        float currentValue = baseVal + sensorEvent.values[0];
        currentMiles = Math.floor(currentValue/2000);
        float stepCount = 0;

        switch (sensorType) {
            case TYPE_ACCELEROMETER:
                // Set the accelerometer sensor text view to the light sensor
                // string from the resources, with the placeholder filled in.
                mTextSensorAccelerometer.setText(getResources().getString(
                        R.string.label_accelerometer, currentValue));
                break;
            case Sensor.TYPE_STEP_COUNTER:
                mTextSensorPedometer.setText(getResources().getString(
                        R.string.label_pedometer, currentValue));
                mTextMilesSaved.setText("{currentMiles}");
                break;
            default:
                // do nothing
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}