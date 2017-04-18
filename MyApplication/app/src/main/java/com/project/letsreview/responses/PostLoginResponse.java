package com.project.letsreview.responses;

/**
 * Created by saurabhgupta on 17/04/17.
 */

public class PostLoginResponse extends GenericResponse{
    private String username;
    private String session_token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionToken() {
        return session_token;
    }

    public void setSession_token(String sessionToken) {
        this.session_token = session_token;
    }
}
