package com.project.letsreview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.letsreview.requests.SignUpRequest;
import com.project.letsreview.responses.GenericResponse;
import com.project.letsreview.validators.METValidators;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private MaterialEditText name;
    private MaterialEditText phoneNumber;
    private MaterialEditText emailId;
    private MaterialEditText username;
    private MaterialEditText password;
    private Button submitButton;

    private METValidators validators;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle(R.string.sign_up);
        validators = new METValidators(this.getApplicationContext());

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
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.6:8080/letsreview/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        APIService apiService = retrofit.create(APIService.class);
        Call<GenericResponse> result = apiService.createUser(request);
        result.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                Toast.makeText(SignUpActivity.this, response.body().getMessage(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Something went wrong",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

}
