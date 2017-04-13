package com.project.letsreview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.letsreview.adapters.ReviewsListAdapter;
import com.project.letsreview.responses.GetReviewsResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_reviews);
        ListView listView = (ListView) findViewById(R.id.listview_reviews);

        List<GetReviewsResponse.Review> list = new ArrayList<>();

        GetReviewsResponse.Review review = new GetReviewsResponse.Review();
        GetReviewsResponse.User user = new GetReviewsResponse.User();
        user.setName("saurabh gupta");
        user.setUsername("saurabhg476");

        review.setBody("This is a good company for a fresher. Work life balance is also good. People are supportive too.");
        review.setCreated_on(new Date());
        review.setRating(5);
        review.setUser(user);

        GetReviewsResponse.Review review2 = new GetReviewsResponse.Review();
        GetReviewsResponse.User user2 = new GetReviewsResponse.User();
        user2.setName("aman mokama");
        user2.setUsername("");

        review2.setBody("Compensation not upto expectations");
        review2.setCreated_on(new Date());
        review2.setRating(1);
        review2.setUser(user2);

        GetReviewsResponse.Review review3 = new GetReviewsResponse.Review();
        GetReviewsResponse.User user3 = new GetReviewsResponse.User();
        user3.setName("surendra");
        user3.setUsername("");

        review3.setBody("The hell I work in flipkart. I don't care.");
        review3.setCreated_on(new Date());
        review3.setRating(5);
        review3.setUser(user3);


        GetReviewsResponse.Review review4 = new GetReviewsResponse.Review();
        GetReviewsResponse.User user4 = new GetReviewsResponse.User();
        user4.setName("yash gupta");
        user4.setUsername("");

        review4.setBody("My team is the best");
        review4.setCreated_on(new Date());
        review4.setRating(4);
        review4.setUser(user4);





        list.add(review);
        list.add(review2);
        list.add(review3);
        list.add(review4);
        list.add(review);
        list.add(review2);
        list.add(review3);
        list.add(review4);
        list.add(review);
        list.add(review2);
        list.add(review3);
        list.add(review4);




        ArrayAdapter<GetReviewsResponse.Review> adapter = new ReviewsListAdapter(GetReviewsActivity.this,R.layout.list_item_review,list);
        listView.setAdapter(adapter);
    }



}
