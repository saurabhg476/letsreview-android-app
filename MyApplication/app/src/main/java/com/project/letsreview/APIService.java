package com.project.letsreview;

import com.project.letsreview.requests.SignUpRequest;
import com.project.letsreview.responses.GenericResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by saurabhgupta on 10/04/17.
 */

public interface APIService {

    @POST("signup")
    Call<GenericResponse> createUser(@Body SignUpRequest request);
}
