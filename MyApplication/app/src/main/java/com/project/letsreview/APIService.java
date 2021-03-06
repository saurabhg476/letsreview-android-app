package com.project.letsreview;

import com.project.letsreview.requests.DeleteReviewsRequest;
import com.project.letsreview.requests.PostLoginRequest;
import com.project.letsreview.requests.PostReviewsRequest;
import com.project.letsreview.requests.PostTopicsRequest;
import com.project.letsreview.requests.SignUpRequest;
import com.project.letsreview.responses.GenericResponse;
import com.project.letsreview.responses.GetReviewsResponse;
import com.project.letsreview.responses.GetTopicsResponse;
import com.project.letsreview.responses.PostLoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by saurabhgupta on 10/04/17.
 */

public interface APIService {


    @POST("signup")
    Call<GenericResponse> createUser(@Body SignUpRequest request);

    @GET("reviews/{topicName}")
    Call<GetReviewsResponse> getReviews(@Path("topicName") String topicName,@Query("page") int page, @Query("per_page") int perPage);

    @POST("reviews")
    Call<GenericResponse> postReviews(@Body PostReviewsRequest request);

    @GET("topics")
    Call<GetTopicsResponse> getTopics(@Query("q") String query);

    @POST("login")
    Call<PostLoginResponse> login(@Body PostLoginRequest request);

    @POST("topics")
    Call<GenericResponse> postTopics(@Body PostTopicsRequest request);

    @HTTP(method = "DELETE",path="reviews",hasBody = true)
    Call<GenericResponse> deleteReview(@Body DeleteReviewsRequest request);
}
