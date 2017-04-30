package com.project.letsreview.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.letsreview.Constants;
import com.project.letsreview.R;
import com.project.letsreview.adapters.ReviewsListAdapter;
import com.project.letsreview.responses.GetReviewsResponse;
import com.project.letsreview.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetReviewsActivity extends AppCompatActivity {

    private ArrayAdapter<GetReviewsResponse.Review> adapter;
    private ProgressDialog pd;
    private TextView topicSummary;
    private String sTopicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_reviews);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialiseComponents();
        callApi(sTopicName);
    }

    private void callApi(String sTopicName){
        Call<GetReviewsResponse> result = Util.getAPIService(this).getReviews(sTopicName);
        result.enqueue(new Callback<GetReviewsResponse>() {
            @Override
            public void onResponse(Call<GetReviewsResponse> call, Response<GetReviewsResponse> response) {
                List<GetReviewsResponse.Review> list = response.body().getList();
                adapter.addAll(list);
                topicSummary.setText(response.body().getTopicSummary());
                pd.hide();
            }

            @Override
            public void onFailure(Call<GetReviewsResponse> call, Throwable t) {
                pd.hide();
                Toast.makeText(GetReviewsActivity.this, "Something went wrong",
                        Toast.LENGTH_LONG).show();
            }
        });

        pd.show();

    }

    private void initialiseComponents(){
        pd = new ProgressDialog(this);
        pd.setMessage("fetching reviews ...");
        pd.setCancelable(false);

        adapter = new ReviewsListAdapter(GetReviewsActivity.this,R.layout.list_item_review,new ArrayList<GetReviewsResponse.Review>());
        ListView listView = (ListView) findViewById(R.id.listview_reviews);
        TextView topicName = (TextView) findViewById(R.id.topic_name);
        topicSummary = (TextView) findViewById(R.id.topic_summary);

        sTopicName = getIntent().getStringExtra(Constants.TOPIC_NAME);
        String sTopicSummary = getIntent().getStringExtra(Constants.TOPIC_SUMMARY);

        topicName.setText(sTopicName);
        topicSummary.setText(sTopicSummary);

        listView.setAdapter(adapter);

    }

}
