package com.project.letsreview.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.project.letsreview.Constants;
import com.project.letsreview.R;
import com.project.letsreview.adapters.EndlessRecyclerViewScrollListener;
import com.project.letsreview.adapters.RecycleViewReviewsAdapter;
import com.project.letsreview.adapters.RecyclerItemClickListener;
import com.project.letsreview.requests.DeleteReviewsRequest;
import com.project.letsreview.responses.GenericResponse;
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
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_reviews);
        createReviewButton = (FloatingActionButton) findViewById(R.id.action_create_review);
        initialiseComponents();
        callApi(sTopicName,0,20);

    }

    private void handleIntent(){
        adapter.setData(new ArrayList<GetReviewsResponse.Review>());
        adapter.notifyDataSetChanged();
        callApi(sTopicName,0,20);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent();
    }

    private void callApi(String sTopicName, int page, int perPage){

        Call<GetReviewsResponse> result = Util.getAPIService(this).getReviews(sTopicName, page,perPage);
        result.enqueue(new Callback<GetReviewsResponse>() {
            @Override
            public void onResponse(Call<GetReviewsResponse> call, Response<GetReviewsResponse> response) {
                List<GetReviewsResponse.Review> list = response.body().getList();
                adapter.getData().addAll(list);
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
        pd.setMessage("fetching reviews ...");
        pd.show();

    }

    private void initialiseComponents(){
        pd = new ProgressDialog(this);
        pd.setCancelable(false);

        TextView topicName = (TextView) findViewById(R.id.topic_name);
        topicSummary = (TextView) findViewById(R.id.topic_summary);

        sTopicName = getIntent().getStringExtra(Constants.TOPIC_NAME);
        String sTopicSummary = getIntent().getStringExtra(Constants.TOPIC_SUMMARY);

        topicName.setText(sTopicName);
        topicSummary.setText(sTopicSummary);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_reviews);
        adapter = new RecycleViewReviewsAdapter(new ArrayList<GetReviewsResponse.Review>());

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, final int position) {
                showOptionsDialog(position);
            }

            @Override public void onLongItemClick(View view, int position) {
            }
        }));

        recyclerView.setOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                callApi(sTopicName,page,20);
            }
        });
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

    private void showOptionsDialog(final int position){
        //if user is logged in
        if(Util.isLoggedIn(GetReviewsActivity.this)){
            //Get username from shared preferences
            SharedPreferences sharedPref = GetReviewsActivity.this.getSharedPreferences(
                    getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
            final String username = sharedPref.getString(getString(R.string.username),"");

            //check if the review is given by user
            if(adapter.getData().get(position).getUser().getUsername().equals(username)){

                final CharSequence options[] = new CharSequence[] {"Delete Review"};
                AlertDialog.Builder builder = new AlertDialog.Builder(GetReviewsActivity.this);
                builder.setTitle("Choose an action");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callDeleteApi(username,position);
                    }
                });
                builder.show();
            }
        }
    }

    private void callDeleteApi(String username, final int position){
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        String sessionToken = sharedPref.getString(getString(R.string.session_token),"");

        DeleteReviewsRequest request = new DeleteReviewsRequest();
        request.setUsername(username);
        request.setSession_token(sessionToken);
        request.setTopic_name(sTopicName);

        Call<GenericResponse> result = Util.getAPIService(this).deleteReview(request);
        result.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                pd.hide();
                if("SUCCESS".equals(response.body().getStatus())){
                    adapter.getData().remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(GetReviewsActivity.this,"review has been deleted successfully",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(GetReviewsActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                pd.hide();
                Toast.makeText(GetReviewsActivity.this, getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG).show();
            }
        });
        pd.setMessage("deleting review ...");
        pd.show();
    }


}
