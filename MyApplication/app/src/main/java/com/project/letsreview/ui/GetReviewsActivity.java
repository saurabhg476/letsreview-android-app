package com.project.letsreview.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.project.letsreview.Constants;
import com.project.letsreview.R;
import com.project.letsreview.adapters.RecycleViewReviewsAdapter;
import com.project.letsreview.responses.GetReviewsResponse;
import com.project.letsreview.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetReviewsActivity extends AppCompatActivity {

    private RecycleViewReviewsAdapter adapter;
    private ProgressDialog pd;
    private TextView topicSummary;
    private String sTopicName;
    private FloatingActionButton createReviewButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_reviews);
        createReviewButton = (FloatingActionButton) findViewById(R.id.action_create_review);
        initialiseComponents();
        callApi(sTopicName);
    }

    private void callApi(String sTopicName){
        Call<GetReviewsResponse> result = Util.getAPIService(this).getReviews(sTopicName);
        result.enqueue(new Callback<GetReviewsResponse>() {
            @Override
            public void onResponse(Call<GetReviewsResponse> call, Response<GetReviewsResponse> response) {
                List<GetReviewsResponse.Review> list = response.body().getList();
                adapter.setData(list);
                adapter.notifyDataSetChanged();
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

        TextView topicName = (TextView) findViewById(R.id.topic_name);
        topicSummary = (TextView) findViewById(R.id.topic_summary);

        sTopicName = getIntent().getStringExtra(Constants.TOPIC_NAME);
        String sTopicSummary = getIntent().getStringExtra(Constants.TOPIC_SUMMARY);

        topicName.setText(sTopicName);
        topicSummary.setText(sTopicSummary);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_reviews);
        adapter = new RecycleViewReviewsAdapter(new ArrayList<GetReviewsResponse.Review>());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setCreateReviewButtonOnClickListener();
    }


    private void setCreateReviewButtonOnClickListener(){
        createReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.isLoggedIn(GetReviewsActivity.this)){
                    launchPostReviewsActivity();
                }else{
                    launchSignUpActivity();
                }
            }
        });
    }

    private void launchPostReviewsActivity(){
        Intent intent = new Intent(this,PostReviewsActivity.class);
        intent.putExtra(Constants.TOPIC_NAME,sTopicName);
        startActivity(intent);
    }

    private void launchSignUpActivity(){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }

}
