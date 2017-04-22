package com.project.letsreview.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

    ArrayAdapter<GetReviewsResponse.Review> adapter;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_reviews);

        pd = new ProgressDialog(this);
        adapter = new ReviewsListAdapter(GetReviewsActivity.this,R.layout.list_item_review,new ArrayList<GetReviewsResponse.Review>());
        ListView listView = (ListView) findViewById(R.id.listview_reviews);
        listView.setAdapter(adapter);

        Intent intent = getIntent();

        String topicName = intent.getStringExtra("topicName");
        callApi(topicName);

    }

    private void callApi(String topicName){
        Call<GetReviewsResponse> result = Util.getAPIService(this).getReviews(topicName);
        result.enqueue(new Callback<GetReviewsResponse>() {
            @Override
            public void onResponse(Call<GetReviewsResponse> call, Response<GetReviewsResponse> response) {
                List<GetReviewsResponse.Review> list = response.body().getList();
                adapter.addAll(list);
                pd.hide();
            }

            @Override
            public void onFailure(Call<GetReviewsResponse> call, Throwable t) {
                pd.hide();
                Toast.makeText(GetReviewsActivity.this, "Something went wrong",
                        Toast.LENGTH_LONG).show();
            }
        });
        pd.setMessage("fetching reviews ...");
        pd.show();

    }


}
