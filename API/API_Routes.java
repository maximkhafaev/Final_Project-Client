package com.maximkhafaev.pilotquiz.API;

import com.maximkhafaev.pilotquiz.API_Data_Models.Login_Data;
import com.maximkhafaev.pilotquiz.API_Data_Models.Login_Response;
import com.maximkhafaev.pilotquiz.API_Data_Models.Question_Model;
import com.maximkhafaev.pilotquiz.API_Data_Models.Test_Info;
import com.maximkhafaev.pilotquiz.API_Data_Models.Test_Result;
import com.maximkhafaev.pilotquiz.API_Data_Models.User_Choice;
import com.maximkhafaev.pilotquiz.API_Data_Models.User_Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API_Routes {

    @POST("users/login")
    Call<Login_Response> login(@Body Login_Data login_data);

    @GET("users")
    Call<User_Model> user(@Header("Authorization") String authHeader);

    @GET("available_tests")
    Call<List<Test_Info>> getAvailableTests(@Header("Authorization") String authHeader);

    @GET("questions/{id}")
    Call<List<Question_Model>> getQuestions(@Header("Authorization") String authHeader, @Path("id") int testid);

    @POST("user_choices")
    Call<Void> sendAnswer(@Header("Authorization") String authHeader, @Body User_Choice user_choice);

    @GET("tests/isPassed/{id}")
    Call<Test_Result> getTestResult(@Header("Authorization") String authHeader, @Path("id") int testid);
}
