package com.project.letsreview.requests;

import java.io.Serializable;

/**
 * Created by saurabhgupta on 02/07/17.
 */

public class DeleteReviewsRequest implements Serializable {
    private String username;
    private String topic_name;
    private String session_token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }
}
