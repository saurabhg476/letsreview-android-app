package com.project.letsreview.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.letsreview.R;
import com.project.letsreview.responses.GetTopicsResponse;

import java.util.List;

/**
 * Created by saurabhgupta on 15/04/17.
 */

public class TopicsListAdapter extends ArrayAdapter<GetTopicsResponse.TopicResponseObject>{

    public TopicsListAdapter(Context context, int resourceId){
        super(context,resourceId);
    }

    public TopicsListAdapter(Context context, int resource, List<GetTopicsResponse.TopicResponseObject> list){
        super(context,resource,list);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_item_topic,null);
        }

        GetTopicsResponse.TopicResponseObject topic = getItem(position);

        if(topic != null){
            TextView name = (TextView) v.findViewById(R.id.textview_topic_name);
            TextView summary = (TextView) v.findViewById(R.id.textview_topic_summary);

            name.setText(topic.getName());
            summary.setText(topic.getSummary());
        }
        return v;
    }

}
