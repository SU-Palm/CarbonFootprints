package com.example.carbonfootprints;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

class TimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TimeLineModel> timeLineModelList;
    private Context context;

    TimeLineAdapter(Context context, List<TimeLineModel> timeLineModelList) {
        this.timeLineModelList = timeLineModelList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_timeline, parent, false);
        return new ViewHolder(view, viewType);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).textView.setText(timeLineModelList.get(position).getName());
        ((ViewHolder) holder).textViewDescription.setText(timeLineModelList.get(position).getDescription());
        ((ViewHolder)holder).textViewTime.setText(timeLineModelList.get(position).getTime());

       /* if (timeLineModelList.get(position).getStatus().equals("inactive"))
            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(ic_remove_circle_outline_black_24dp));
        else
            ((ViewHolder) holder).timelineView.setMarker(context.getDrawable(ic_check_circle_black_24dp));*/
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return timeLineModelList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TimelineView timelineView;
        TextView textView, textViewDescription, textViewTime;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
           /* timelineView = itemView.findViewById(R.id.row_timeline_layout_time_marker);
            textView = itemView.findViewById(R.id.row_timeline_layout_text_view_name);
            textViewDescription = itemView.findViewById(R.id.row_timeline_layout_text_view_description);
            textViewTime = itemView.findViewById(R.id.row_timeline_layout_text_view_time);
*/
            timelineView.initLine(viewType);
        }
    }}