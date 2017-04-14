package com.project.letsreview.responses;

import java.util.List;

/**
 * Created by saurabhgupta on 14/04/17.
 */

public class GetTopicsResponse extends GenericResponse{

    private List<String> topicNames;

    public List<String> getTopicNames() {
        return topicNames;
    }

    public void setTopicNames(List<String> topicNames) {
        this.topicNames = topicNames;
    }
}
