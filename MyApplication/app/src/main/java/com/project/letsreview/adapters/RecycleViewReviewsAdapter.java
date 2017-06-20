package com.project.letsreview.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.project.letsreview.R;
import com.project.letsreview.responses.GetReviewsResponse;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by saurabhgupta on 30/04/17.
 */

public class RecycleViewReviewsAdapter extends RecyclerView.Adapter<RecycleViewReviewsAdapter.ViewHolder>{

    private class VIEW_TYPES{
        public static final int NORMAL = 1;
        public static final int FOOTER = 2;
    }

    private List<GetReviewsResponse.Review> data;

    public void setData(List<GetReviewsResponse.Review> data){
        this.data = data;
    }
    public RecycleViewReviewsAdapter(List<GetReviewsResponse.Review> data){
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView reviewerName;
        RatingBar reviewRating;
        TextView reviewBody;
        TextView reviewDate;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewerName = (TextView) itemView.findViewById(R.id.reviewer_name);
            reviewRating = (RatingBar) itemView.findViewById(R.id.ratingBar);
            reviewBody = (TextView) itemView.findViewById(R.id.review_body);
            reviewDate = (TextView) itemView.findViewById(R.id.review_date);
        }
    }

    @Override
    public RecycleViewReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleViewReviewsAdapter.ViewHolder holder, int position) {
        GetReviewsResponse.Review review = data.get(position);
        holder.reviewerName.setText(review.getUser().getName());
        holder.reviewRating.setRating(review.getRating());
        holder.reviewBody.setText(review.getBody());

        holder.reviewDate.setText(DateFormat.getDateInstance().format(review.getCreated_on()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
