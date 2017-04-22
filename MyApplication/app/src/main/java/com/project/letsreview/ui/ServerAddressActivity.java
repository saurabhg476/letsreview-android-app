package com.project.letsreview.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.letsreview.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.commons.lang3.StringUtils;


public class ServerAddressActivity extends AppCompatActivity {

    private MaterialEditText serverAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        final SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        if(sharedPref.contains(getString(R.string.server_address))){
            callHomeActivity();
        }

        serverAddress = (MaterialEditText) findViewById(R.id.server_address);
        Button submitButton = (Button) findViewById(R.id.submit_button);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.server_address), serverAddress.getText().toString());
                    editor.commit();
                    callHomeActivity();
                }else{
                    Toast.makeText(ServerAddressActivity.this,"please enter valid server address",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validate(){
        return StringUtils.isNotBlank(serverAddress.getText().toString());
    }

    private void callHomeActivity(){
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}

