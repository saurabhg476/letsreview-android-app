package com.project.letsreview.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.letsreview.APIService;
import com.project.letsreview.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by saurabhgupta on 14/04/17.
 */
public class Util {

    private Util(){
        //private constructor to prevent instantiation.
    }

    private static APIService apiService;


    public static APIService getAPIService(Context context){
        if(apiService == null)
            initializeAPIService(context);
        return apiService;
    }


    private static void initializeAPIService(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);

        //aws address for prod branch
        String serverAddress = "letsreview.us-east-2.elasticbeanstalk.com";
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .build();
        String baseUrl = "http://" + serverAddress +"/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build();
        apiService = retrofit.create(APIService.class);
    }

    public static boolean isLoggedIn(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preferences_file_key),Context.MODE_PRIVATE);
        return sharedPref.contains(context.getString(R.string.session_token));
    }
}
