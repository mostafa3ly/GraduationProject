package com.example.mostafa.graduationproject;

import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mosta on 16/12/2017.
 */

public class Utils {
    public static final int ESSAY_QUESTION_TYPE = 1;
    public static final int SINGLE_CHOICE_QUESTION_TYPE = 2;
    public static final int MULTI_CHOICE_QUESTION_TYPE = 3;
    private static final String BASE_URL = "https://infinitysurvey.herokuapp.com/";
    public static final String FIRST_NAME_PROPERTY_KEY = "firstname";
    public static final String LAST_NAME_PROPERTY_KEY = "lastname";
    public static final String EMAIL_PROPERTY_KEY = "email";
    public static final String PASSWORD_PROPERTY_KEY = "password";
    public static final String TOKEN_PROPERTY_KEY  = "token";


    public static Retrofit getApiClient()
    {
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(new OkHttpClient.Builder().build())
            .build();
    }

    public interface ApiEndPointsInterface
    {
        @POST("api/user/")
        Call<JsonObject> createUser(@Body JsonObject user);

        @POST("api/user/login")
        Call<JsonObject> login(@Body JsonObject user);

    }
}