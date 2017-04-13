package com.project.letsreview.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.project.letsreview.R;
import com.project.letsreview.responses.GetReviewsResponse;

import java.util.List;

/**
 * Created by saurabhgupta on 11/04/17.
 */

public class ReviewsListAdapter extends ArrayAdapter<GetReviewsResponse.Review>{

    public ReviewsListAdapter(Context context,int resourceId){
        super(context,resourceId);
    }

    public ReviewsListAdapter(Context context, int resource, List<GetReviewsResponse.Review> list){
        super(context,resource,list);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review,null);
        }

        GetReviewsResponse.Review review = getItem(position);

        if(review != null){
            TextView reviewerName = (TextView) v.findViewById(R.id.reviewer_name);
            RatingBar reviewRating = (RatingBar) v.findViewById(R.id.ratingBar);
            TextView reviewBody = (TextView) v.findViewById(R.id.review_body);

            reviewerName.setText(review.getUser().getName());
            reviewRating.setRating(review.getRating());
            reviewBody.setText(review.getBody());
        }
        return v;
    }
}
