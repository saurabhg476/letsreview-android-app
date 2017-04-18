package com.project.letsreview.responses;

import java.util.List;

/**
 * Created by saurabhgupta on 14/04/17.
 */

public class GetTopicsResponse extends GenericResponse{

    private List<TopicResponseObject> topicsList;

    public List<TopicResponseObject> getTopicsList() {
        return topicsList;
    }

    public void setTopicsList(List<TopicResponseObject> topicsList) {
        this.topicsList = topicsList;
    }

    public static class TopicResponseObject{
        private String name;
        private String summary;

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
    }
}
