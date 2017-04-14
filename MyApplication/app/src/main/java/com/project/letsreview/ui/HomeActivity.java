package com.project.letsreview.ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.project.letsreview.R;
import com.project.letsreview.responses.GetTopicsResponse;
import com.project.letsreview.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pd = new ProgressDialog(this);
        ListView listView = (ListView) findViewById(R.id.listview_topics);
        adapter = new ArrayAdapter<String>(this,R.layout.list_item_topic,R.id.list_item_topic_textview,new ArrayList<String>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String topicName = adapter.getItem(position);
            }
        });


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    private void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            callApi(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void callApi(String query){
        Call<GetTopicsResponse> result = Util.getAPIService().getTopics(query);
        result.enqueue(new Callback<GetTopicsResponse>() {
            @Override
            public void onResponse(Call<GetTopicsResponse> call, Response<GetTopicsResponse> response) {
                pd.hide();
                GetTopicsResponse getTopicsResponse = response.body();
                List<String> topicNames = getTopicsResponse.getTopicNames();
                adapter.addAll(topicNames);
            }

            @Override
            public void onFailure(Call<GetTopicsResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Something went wrong",
                        Toast.LENGTH_LONG).show();
            }
        });
        pd.setMessage("searching ...");
        pd.show();
    }
}
