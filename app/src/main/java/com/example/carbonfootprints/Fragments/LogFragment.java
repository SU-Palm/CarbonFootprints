package com.example.carbonfootprints.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carbonfootprints.R;
import com.example.carbonfootprints.TimeLineAdapter;
import com.example.carbonfootprints.TimeLineModel;
import com.example.carbonfootprints.Timeline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public TimeLineAdapter timeLineAdapter;
    public RecyclerView timeLineRecyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    List<TimeLineModel> timeLineModelList;
    TimeLineModel[] timeLineModel;
    Context context;
    LinearLayoutManager linearLayoutManager;

    String[] name = {"Event 1", "Event 2", "Event 3", "Event 4", "Event 5", "Event 6", "Event 7"};
    String[] status = {"active", "inactive", "inactive", "inactive", "inactive", "inactive", "inactive"};
    String[] description = {"Description 1","Description 2","Description 3","Description 4","Description 5","Description 6","Description 7"};
    String[] time = {"11:00 PM", "10:03 AM", "10:03 PM", "10:03 PM", "10:03 PM", "10:03 PM", "10:03 PM"};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogFragment newInstance(String param1, String param2) {
        LogFragment fragment = new LogFragment();
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
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        int size = name.length;
        timeLineModelList = new ArrayList<>();
        timeLineModel = new TimeLineModel[size];
        context = getContext();
        linearLayoutManager = new LinearLayoutManager(view.getContext());

        for (int i = 0; i < size; i++) {
            timeLineModel[i] = new TimeLineModel();
            timeLineModel[i].setName(name[i]);
            timeLineModel[i].setStatus(status[i]);
            timeLineModel[i].setDescription(description[i]);
            timeLineModel[i].setTime(time[i]);
            timeLineModelList.add(timeLineModel[i]);
        }

        timeLineRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        timeLineRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(timeLineRecyclerView.getContext(), linearLayoutManager.getOrientation());
        timeLineRecyclerView.addItemDecoration(dividerItemDecoration);  //for divider

        // timeLineRecyclerView.setAdapter(new TimeLineAdapter(view.getContext(), timeLineModelList));

        return view;
    }

    public void getUserTrips() {
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
                    Map<String, Map<String, Object>> trips = (Map<String, Map<String, Object>>) task.getResult().getValue();
                    Map<String, Object> trip1 = (Map<String, Object>) trips.get(0);
                    Map<String, Object> trip2 = (Map<String, Object>) trips.get(1);
                    Map<String, Object> trip3 = (Map<String, Object>) trips.get(2);
                    Map<String, Object> trip4 = (Map<String, Object>) trips.get(3);
                    Map<String, Object> trip5 = (Map<String, Object>) trips.get(4);
                    Map<String, Object> trip6 = (Map<String, Object>) trips.get(5);
                    Map<String, Object> trip7 = (Map<String, Object>) trips.get(6);


                }
            }
        });
    }
}