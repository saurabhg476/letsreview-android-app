package com.project.letsreview.ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.project.letsreview.Constants;
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

    private ArrayAdapter<GetTopicsResponse.Topic> adapter;
    private ProgressDialog pd;
    private FloatingActionButton createTopicButton;
    private FloatingActionButton createReviewButton;
    private FloatingActionsMenu fabMenu;
    private ListView listView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        fabMenu.collapseImmediately();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.logout){
            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(getString(R.string.session_token));
            editor.remove(getString(R.string.username));
            editor.apply();
            invalidateOptionsMenu();
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

        initialiseComponents();
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
        Call<GetTopicsResponse> result = Util.getAPIService(this).getTopics(query);
        result.enqueue(new Callback<GetTopicsResponse>() {
            @Override
            public void onResponse(Call<GetTopicsResponse> call, Response<GetTopicsResponse> response) {
                pd.hide();
                GetTopicsResponse getTopicsResponse = response.body();
                List<GetTopicsResponse.Topic> topicsList = getTopicsResponse.getTopicsList();

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
        pd.show();
    }

    private void launchCreateTopicActivity(){
        Intent intent = new Intent(this,PostTopicsActivity.class);
        startActivity(intent);
    }

    private void launchSignUpActivity(String function){
        Intent intent = new Intent(this,SignUpActivity.class);
        intent.putExtra("function",function);
        startActivity(intent);
    }

    private void launchPostReviewsActivity(){
        Intent intent = new Intent(this,PostReviewsActivity.class);
        startActivity(intent);
    }

    private void initialiseComponents(){
        fabMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        createReviewButton = (FloatingActionButton) findViewById(R.id.action_create_review);
        createTopicButton = (FloatingActionButton) findViewById(R.id.action_create_topic);

        setCreateTopicButtonOnClickListener();
        setCreateReviewButtonOnClickListener();
        pd = new ProgressDialog(this);
        pd.setMessage("searching ...");
        pd.setCancelable(false);

        listView = (ListView) findViewById(R.id.listview_topics);
        adapter = new TopicsListAdapter(this,R.layout.list_item_topic,new ArrayList<GetTopicsResponse.Topic>());
        listView.setAdapter(adapter);
        setListViewOnItemClickListener();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    private void setCreateTopicButtonOnClickListener(){
        createTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Util.isLoggedIn(HomeActivity.this)){
                    launchCreateTopicActivity();
                }else{
                    launchSignUpActivity("createTopic");
                }
            }
        });
    }

    private void setCreateReviewButtonOnClickListener(){
        createReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.isLoggedIn(HomeActivity.this)){
                    launchPostReviewsActivity();
                }else{
                    launchSignUpActivity("createReview");
                }
            }
        });
    }

    private void setListViewOnItemClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetTopicsResponse.Topic topic = adapter.getItem(position);
                Intent intent = new Intent(HomeActivity.this,GetReviewsActivity.class);
                intent.putExtra(Constants.TOPIC_NAME,topic.getName());
                intent.putExtra(Constants.TOPIC_SUMMARY,topic.getSummary());

                startActivity(intent);
            }
        });
    }

    @Override public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (fabMenu.isExpanded()) {

                Rect outRect = new Rect();
                fabMenu.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
                    fabMenu.collapseImmediately();
            }
        }

        return super.dispatchTouchEvent(event);
    }

}
