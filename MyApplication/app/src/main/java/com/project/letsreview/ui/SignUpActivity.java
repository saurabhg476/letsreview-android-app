package com.project.letsreview.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.letsreview.R;
import com.project.letsreview.requests.SignUpRequest;
import com.project.letsreview.responses.GenericResponse;
import com.project.letsreview.utils.Util;
import com.project.letsreview.validators.METValidators;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private MaterialEditText name;
    private MaterialEditText phoneNumber;
    private MaterialEditText emailId;
    private MaterialEditText username;
    private MaterialEditText password;
    private Button submitButton;
    private ProgressDialog pd;

    private METValidators validators;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle(R.string.sign_up);
        validators = METValidators.getMETValidators(this.getApplicationContext());

        name = (MaterialEditText) findViewById(R.id.name);
        phoneNumber = (MaterialEditText) findViewById(R.id.phone_no);
        emailId = (MaterialEditText) findViewById(R.id.email_id);
        username = (MaterialEditText) findViewById(R.id.username);
        password = (MaterialEditText) findViewById(R.id.password);
        submitButton = (Button) findViewById(R.id.submit_button);

        name.addValidator(validators.getNameValidator());
        phoneNumber.addValidator(validators.getPhoneNoValidator());
        emailId.addValidator(validators.getEmailIdValidator());
        username.addValidator(validators.getUsernameValidator());
        password.addValidator(validators.getPasswordValidator());


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    SignUpRequest request = generateRequest();
                    callApi(request);
                }
            }
        });

        pd = new ProgressDialog(SignUpActivity.this);
        pd.setCancelable(false);

    }

    private boolean validate(){
        return (name.validate() && phoneNumber.validate() && emailId.validate() && username.validate() && password.validate());
    }

    private SignUpRequest generateRequest(){
        SignUpRequest request = new SignUpRequest();

        request.setName(name.getText().toString());

        if(phoneNumber.getText() != null) {
            request.setPhone_no(phoneNumber.getText().toString());
        }

        request.setEmail_id(emailId.getText().toString());
        request.setUsername(username.getText().toString());
        request.setPassword(password.getText().toString());
        return request;
    }

    private void callApi(SignUpRequest request){

        Call<GenericResponse> result = Util.getAPIService().createUser(request);
        result.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {

                String status = response.body().getStatus();
                if("SUCCESS".equalsIgnoreCase(status)){
                    pd.hide();
                    Toast.makeText(SignUpActivity.this, getString(R.string.signup_successful_continue_login),
                            Toast.LENGTH_LONG).show();

                    submitButton.setText(getString(R.string.next));
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                }else{
                    pd.hide();
                    Toast.makeText(SignUpActivity.this, response.body().getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, getString(R.string.something_went_wrong),
                        Toast.LENGTH_LONG).show();
            }
        });
        pd.setMessage("please wait ...");
        pd.show();

    }

}
