package com.project.letsreview.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.letsreview.R;

import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        listView = (ListView) findViewById(R.id.listview_topics);

        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query){
        List<String> list = new ArrayList<>();
        list.add(query + "1");
        list.add(query + "2");
        list.add(query + "3");
        list.add(query + "4");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchableActivity.this,R.layout.list_item_topic,R.id.list_item_topic_textview,list);
        listView.setAdapter(adapter);
    }
}
