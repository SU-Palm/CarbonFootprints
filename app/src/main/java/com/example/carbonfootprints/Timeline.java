package com.example.carbonfootprints;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Timeline extends AppCompatActivity {

    RecyclerView recyclerView;
    String[] name = {"Event 1", "Event 2", "Event 3"};
    String[] status = {"active", "inactive", "inactive"};
    String[] description = {"Description 1","Description 2","Description 3"};
    String[] time = {"11:00 PM", "10:03 AM", "10:03 PM"};

    List<TimeLineModel> timeLineModelList;
    TimeLineModel[] timeLineModel;
    Context context;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        timeLineModelList = new ArrayList<>();
        int size = name.length;
        timeLineModel = new TimeLineModel[size];
        context = Timeline.this;
        linearLayoutManager = new LinearLayoutManager(this);

        for (int i = 0; i < size; i++) {
            timeLineModel[i] = new TimeLineModel();
            timeLineModel[i].setName(name[i]);
            timeLineModel[i].setStatus(status[i]);
            timeLineModel[i].setDescription(description[i]);
            timeLineModel[i].setTime(time[i]);
            timeLineModelList.add(timeLineModel[i]);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);  //for divider

        //recyclerView.setAdapter(new TimeLineAdapter(context, timeLineModelList));
    }}