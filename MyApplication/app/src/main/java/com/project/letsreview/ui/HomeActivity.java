package com.project.letsreview.ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.project.letsreview.R;
import com.project.letsreview.adapters.TopicsListAdapter;
import com.project.letsreview.responses.GetTopicsResponse;
import com.project.letsreview.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ArrayAdapter<GetTopicsResponse.TopicResponseObject> adapter;
    private ProgressDialog pd;
    private FloatingActionButton fab;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.logout){
            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        MenuItem item = menu.findItem(R.id.logout);
        if(!sharedPref.contains(getString(R.string.session_token))){
            item.setVisible(false);
        }else item.setVisible(true);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = HomeActivity.this.getSharedPreferences(getString(R.string.preferences_file_key),Context.MODE_PRIVATE);
                boolean isSessionTokenPresent = sharedPref.contains(getString(R.string.session_token));
                if(isSessionTokenPresent){
                    Intent intent = new Intent(HomeActivity.this,PostReviewsActivity.class);
                    startActivity(intent);
                }else{
                    //launch signup activity.
                    Intent intent = new Intent(HomeActivity.this,SignUpActivity.class);
                    startActivity(intent);
                }
            }
        });

        pd = new ProgressDialog(this);
        ListView listView = (ListView) findViewById(R.id.listview_topics);
        adapter = new TopicsListAdapter(this,R.layout.list_item_topic,new ArrayList<GetTopicsResponse.TopicResponseObject>());
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetTopicsResponse.TopicResponseObject topic = adapter.getItem(position);
                String topicName = topic.getName();
                Intent intent = new Intent(HomeActivity.this,GetReviewsActivity.class);
                intent.putExtra("topicName",topicName);
                startActivity(intent);
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
                List<GetTopicsResponse.TopicResponseObject> topicsList = getTopicsResponse.getTopicsList();

                adapter.clear();
                adapter.addAll(topicsList);
            }

            @Override
            public void onFailure(Call<GetTopicsResponse> call, Throwable t) {
                pd.hide();
                Toast.makeText(HomeActivity.this, "Something went wrong",
                        Toast.LENGTH_LONG).show();
            }
        });
        pd.setMessage("searching ...");
        pd.show();
    }
}
