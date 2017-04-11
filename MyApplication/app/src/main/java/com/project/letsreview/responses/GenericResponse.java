package com.project.letsreview.responses;

import java.io.Serializable;

/**
 * Created by saurabhgupta on 10/04/17.
 */

public class GenericResponse implements Serializable{
    private String code;
    private String status;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
