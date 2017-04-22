package com.project.letsreview.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.project.letsreview.R;
import com.project.letsreview.responses.GetTopicsResponse;
import com.project.letsreview.utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * This adapter is for autocomplete text view that shows the list of topics.
 * This should not be confused with TopicsListAdapter as that is for home screen
 * and this is for create review screen i.e. where you create the review.
 * Created by saurabhgupta on 21/04/17.
 */

public class TopicsAdapter extends BaseAdapter implements Filterable{

    private List<GetTopicsResponse.Topic> resultList = new ArrayList<GetTopicsResponse.Topic>();
    private Context mContext;

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Nullable
    @Override
    public GetTopicsResponse.Topic getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public TopicsAdapter(Context context){
        mContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null){
            v = LayoutInflater.from(mContext).inflate(R.layout.list_item_topic,parent,false);
        }

        GetTopicsResponse.Topic topic = getItem(position);

        if(topic != null){
            TextView name = (TextView) v.findViewById(R.id.textview_topic_name);
            TextView summary = (TextView) v.findViewById(R.id.textview_topic_summary);

            name.setText(topic.getName());
            summary.setText(topic.getSummary());
        }
        return v;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if(constraint != null) {
                    Call<GetTopicsResponse> call = Util.getAPIService(mContext).getTopics(constraint.toString());
                    GetTopicsResponse response = null;
                    try {
                        response = call.execute().body();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    List<GetTopicsResponse.Topic> list = response.getTopicsList();
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = list;
                    filterResults.count = list.size();
                    return filterResults;
                }
                else return new FilterResults();
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if((List<GetTopicsResponse.Topic>) results.values != null){
                    resultList = (List<GetTopicsResponse.Topic>) results.values;
                    notifyDataSetChanged();
                }

            }
        };
    }

}
