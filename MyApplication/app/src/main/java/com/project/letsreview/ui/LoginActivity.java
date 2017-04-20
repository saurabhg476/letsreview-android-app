package com.project.letsreview.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.project.letsreview.R;
import com.project.letsreview.components.EditText;
import com.project.letsreview.requests.PostLoginRequest;
import com.project.letsreview.responses.PostLoginResponse;
import com.project.letsreview.utils.Util;
import com.project.letsreview.validators.METValidators;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //initialise views
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCancelable(false);

        username =  (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        Button submitButton = (Button) findViewById(R.id.submit_button);

        //add validators
        METValidators metValidators = METValidators.getMETValidators(this.getApplicationContext());
        username.addValidator(metValidators.getUsernameValidator());
        password.addValidator(metValidators.getPasswordValidator());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    callAPI();
                }
            }
        });
    }

    private void callAPI(){
        PostLoginRequest request = new PostLoginRequest();
        request.setUsername(username.getText().toString());
        request.setPassword(password.getText().toString());

        Call<PostLoginResponse> result = Util.getAPIService().login(request);
        result.enqueue(new Callback<PostLoginResponse>() {
            @Override
            public void onResponse(Call<PostLoginResponse> call, Response<PostLoginResponse> response) {
                pd.hide();
                Toast.makeText(LoginActivity.this, response.body().getMessage(),
                        Toast.LENGTH_LONG).show();
                if("SUCCESS".equalsIgnoreCase(response.body().getStatus())){
                    saveSessionToken(response.body().getSessionToken());
                    callPostReviewsActivity();
                }
            }

            @Override
            public void onFailure(Call<PostLoginResponse> call, Throwable t) {
                pd.hide();
                Toast.makeText(LoginActivity.this, getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG).show();
            }
        });
        pd.setMessage("please wait ...");
        pd.show();
    }

    private boolean validate(){
        return (username.validate() && password.validate());
    }

    private void saveSessionToken(String sessionToken){
        SharedPreferences sharedPred = LoginActivity.this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPred.edit();
        editor.putString(getString(R.string.session_token),sessionToken);
        editor.apply();
    }

    private void callPostReviewsActivity(){
        Intent intent = new Intent(LoginActivity.this,PostReviewsActivity.class);
        startActivity(intent);
    }
}
