package com.project.letsreview.requests;

/**
 * Created by saurabhgupta on 20/04/17.
 */

public class PostReviewsRequest {
    private int rating;
    private String body;
    private String username;
    private String topic_name;
    private String session_token;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTopicName() {
        return topic_name;
    }

    public void setTopicName(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getSessionToken() {
        return session_token;
    }

    public void setSessionToken(String session_token) {
        this.session_token = session_token;
    }
}
