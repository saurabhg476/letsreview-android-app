package com.project.letsreview.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.project.letsreview.R;
import com.project.letsreview.components.EditText;
import com.project.letsreview.requests.PostReviewsRequest;
import com.project.letsreview.responses.GenericResponse;
import com.project.letsreview.utils.Util;
import com.project.letsreview.validators.METValidators;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostReviewsActivity extends AppCompatActivity {

    private SimpleRatingBar ratingBar;
    private EditText topicName;
    private EditText content;
    private ProgressDialog pd;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
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
        topicName = (EditText) findViewById(R.id.topic_name);
        content = (EditText) findViewById(R.id.content);
        Button submitButton = (Button) findViewById(R.id.submit_button);

        METValidators validators = METValidators.getMETValidators(this.getApplicationContext());

        topicName.addValidator(validators.getTopicNameValidator());
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
                if(validate()){
                    PostReviewsRequest request = constructPostReviewsRequest();
                    callAPI(request);
                }
            }
        });

    }

    private boolean validate(){
        return topicName.validate() && content.validate();
    }

    private void callAPI(PostReviewsRequest request){
        Call<GenericResponse> result = Util.getAPIService().postReviews(request);
        result.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                pd.hide();
                Toast.makeText(PostReviewsActivity.this, response.body().getMessage(),
                        Toast.LENGTH_LONG).show();

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

}
