package com.project.letsreview.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.project.letsreview.Constants;
import com.project.letsreview.R;
import com.project.letsreview.adapters.TopicsAdapter;
import com.project.letsreview.components.EditText;
import com.project.letsreview.requests.PostReviewsRequest;
import com.project.letsreview.responses.GenericResponse;
import com.project.letsreview.responses.GetTopicsResponse;
import com.project.letsreview.utils.Util;
import com.project.letsreview.validators.METValidators;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostReviewsActivity extends AppCompatActivity {

    private SimpleRatingBar ratingBar;
    private MaterialAutoCompleteTextView topicName;
    private EditText content;
    private ProgressDialog pd;

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener positiveButtonOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        };

        new AlertDialog.Builder(this)
                .setMessage(R.string.exit_message)
                .setCancelable(false)
                .setPositiveButton("Yes",positiveButtonOnClickListener)
                .setNegativeButton("No", null)
                .show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reviews);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pd = new ProgressDialog(this);
        pd.setCancelable(false);

        ratingBar = (SimpleRatingBar) findViewById(R.id.ratingBar);
        topicName = (MaterialAutoCompleteTextView) findViewById(R.id.topic_name);
        topicName.setThreshold(3);
        topicName.setAdapter(new TopicsAdapter(this));
        topicName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetTopicsResponse.Topic topic = (GetTopicsResponse.Topic) parent.getItemAtPosition(position);
                topicName.setText(topic.getName());
                topicName.setSelection(topic.getName().length());
            }
        });

        content = (EditText) findViewById(R.id.content);

        //If this activity is called from GetReviewsActivity, then topic name is passed along.
        if(getIntent().getStringExtra(Constants.TOPIC_NAME) !=  null){
            topicName.setText(getIntent().getStringExtra(Constants.TOPIC_NAME));
            content.requestFocus();
        }

        Button submitButton = (Button) findViewById(R.id.submit_button);

        METValidators validators = METValidators.getMETValidators(this.getApplicationContext());

        content.addValidator(validators.getReviewBodyValidator());


        ratingBar.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                if(rating<1.0f)
                    simpleRatingBar.setRating(1.0f);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content.validate()){
                    PostReviewsRequest request = constructPostReviewsRequest();
                    callAPI(request);
                }
            }
        });

    }

    private void callAPI(PostReviewsRequest request){
        Call<GenericResponse> result = Util.getAPIService(this).postReviews(request);
        result.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                pd.hide();
                Toast.makeText(PostReviewsActivity.this, response.body().getMessage(),
                        Toast.LENGTH_LONG).show();
                if("00".equals(response.body().getCode())){
                    launchGetReviewsActivity(topicName.getText().toString());
                }

            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                pd.hide();
                Toast.makeText(PostReviewsActivity.this, getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG).show();
            }
        });
        pd.setMessage("please wait ...");
        pd.show();
    }

    private PostReviewsRequest constructPostReviewsRequest(){
        PostReviewsRequest request = new PostReviewsRequest();

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        //if username or sessionToken is not present, then do something...? make him login again?
        String username = sharedPref.getString(getString(R.string.username),"");
        String sessionToken = sharedPref.getString(getString(R.string.session_token),"");

        request.setUsername(username);
        request.setSessionToken(sessionToken);
        request.setRating((int) ratingBar.getRating());
        request.setBody(content.getText().toString());
        request.setTopicName(topicName.getText().toString());

        return request;
    }

    public void launchGetReviewsActivity(String topicName){
        Intent intent = new Intent(this, GetReviewsActivity.class);
        intent.putExtra(Constants.TOPIC_NAME,topicName);
        startActivity(intent);
    }
}
