package com.project.letsreview.requests;

import java.io.Serializable;

/**
 * Created by saurabhgupta on 17/04/17.
 */

public class PostLoginRequest implements Serializable{
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
