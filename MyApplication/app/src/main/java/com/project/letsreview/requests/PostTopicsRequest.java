package com.project.letsreview.requests;

/**
 * Created by saurabhgupta on 29/04/17.
 */

public class PostTopicsRequest {
    private String name;
    private String summary;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
