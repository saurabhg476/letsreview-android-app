package com.project.letsreview.responses;

import java.util.Date;
import java.util.List;

/**
 * Created by saurabhgupta on 11/04/17.
 */

public class GetReviewsResponse extends GenericResponse{

    private String topicName;
    private String topicSummary;
    private List<Review> list;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicSummary() {
        return topicSummary;
    }

    public void setTopicSummary(String topicSummary) {
        this.topicSummary = topicSummary;
    }

    public List<Review> getList() {
        return list;
    }

    public void setList(List<Review> list) {
        this.list = list;
    }

    public static class Review{
        private String body;
        private Date created_on;
        private int rating;
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public Date getCreated_on() {
            return created_on;
        }

        public void setCreated_on(Date created_on) {
            this.created_on = created_on;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

    public static class User{
        private String name;
        private String username;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
