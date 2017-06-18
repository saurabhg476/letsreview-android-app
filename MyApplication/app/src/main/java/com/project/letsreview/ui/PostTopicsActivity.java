package com.project.letsreview.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.project.letsreview.Constants;
import com.project.letsreview.R;
import com.project.letsreview.components.EditText;
import com.project.letsreview.requests.PostTopicsRequest;
import com.project.letsreview.responses.GenericResponse;
import com.project.letsreview.utils.Util;
import com.project.letsreview.validators.METValidators;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostTopicsActivity extends AppCompatActivity{

    private EditText topicName;
    private EditText topicSummary;
    private EditText topicDescription;
    private Button submitButton;
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
        setContentView(R.layout.activity_post_topic);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initialiseComponents();
        addValidators();
        addSubmitButtonOnClickListener();

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("please wait ...");
    }

    private void initialiseComponents(){
        topicName = (EditText) findViewById(R.id.topic_name);
        topicSummary = (EditText) findViewById(R.id.topic_summary);
        topicDescription = (EditText) findViewById(R.id.topic_description);
        submitButton = (Button) findViewById(R.id.submit_button);
    }


    private void addValidators(){
        METValidators validators = METValidators.getMETValidators(this.getApplicationContext());
        topicName.addValidator(validators.getTopicNameValidator());
        topicSummary.addValidator(validators.getTopicSummaryValidator());
        topicDescription.addValidator(validators.getTopicDescriptionValidator());
    }

    private void addSubmitButtonOnClickListener(){
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    PostTopicsRequest request = generatePostTopicsRquest();
                    callApi(request);
                }
            }
        });
    }

    private boolean validate(){
        return topicName.validate() && topicSummary.validate() && topicDescription.validate();
    }

    private void callApi(PostTopicsRequest request){
        Call<GenericResponse> result = Util.getAPIService(this).postTopics(request);
        result.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                pd.hide();
                Toast.makeText(PostTopicsActivity.this, response.body().getMessage(),
                        Toast.LENGTH_LONG).show();
                if("00".equals(response.body().getCode())){
                    launchGetReviewsActivity(topicName.getText().toString());
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                pd.hide();
                Toast.makeText(PostTopicsActivity.this, getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG).show();
            }
        });
        pd.show();
    }

    private PostTopicsRequest generatePostTopicsRquest(){
        PostTopicsRequest request = new PostTopicsRequest();
        request.setName(topicName.getText().toString());
        request.setSummary(topicSummary.getText().toString());
        request.setDescription(topicDescription.getText().toString());

        return request;
    }

    public void launchGetReviewsActivity(String topicName){
        Intent intent = new Intent(this,GetReviewsActivity.class);
        intent.putExtra(Constants.TOPIC_NAME,topicName);
        startActivity(intent);
    }
}

