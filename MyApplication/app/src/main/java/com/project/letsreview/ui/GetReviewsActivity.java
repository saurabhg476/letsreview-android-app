package com.project.letsreview.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.project.letsreview.APIService;
import com.project.letsreview.R;
import com.project.letsreview.adapters.ReviewsListAdapter;
import com.project.letsreview.responses.GetReviewsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetReviewsActivity extends AppCompatActivity {

    ArrayAdapter<GetReviewsResponse.Review> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_reviews);

        adapter = new ReviewsListAdapter(GetReviewsActivity.this,R.layout.list_item_review,new ArrayList<GetReviewsResponse.Review>());
        ListView listView = (ListView) findViewById(R.id.listview_reviews);
        listView.setAdapter(adapter);

        String topicName = "novopay";
        callApi(topicName);

    }

    private void callApi(String topicName){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(APIService.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build();
        APIService apiService = retrofit.create(APIService.class);
        Call<GetReviewsResponse> result = apiService.getReviews(topicName);
        result.enqueue(new Callback<GetReviewsResponse>() {
            @Override
            public void onResponse(Call<GetReviewsResponse> call, Response<GetReviewsResponse> response) {
                List<GetReviewsResponse.Review> list = response.body().getList();
                adapter.addAll(list);
            }

            @Override
            public void onFailure(Call<GetReviewsResponse> call, Throwable t) {
                Toast.makeText(GetReviewsActivity.this, "Something went wrong",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


}
