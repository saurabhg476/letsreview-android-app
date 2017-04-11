package com.project.letsreview.requests;

import java.io.Serializable;

/**
 * Created by saurabhgupta on 10/04/17.
 */

public class SignUpRequest implements Serializable{

    private String name;
    private String phone_on;
    private String email_id;
    private String username;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_on() {
        return phone_on;
    }

    public void setPhone_on(String phone_on) {
        this.phone_on = phone_on;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

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
