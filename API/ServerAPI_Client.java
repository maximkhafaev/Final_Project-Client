package com.maximkhafaev.pilotquiz.API;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerAPI_Client {
    private static String BASE_URL = "http://192.168.1.2:3000/";
    private static ServerAPI_Client mInstance;
    private Retrofit retrofit;

    private ServerAPI_Client(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static synchronized ServerAPI_Client getInstance(){
        if (mInstance == null) {
            mInstance = new ServerAPI_Client();
        }
        return mInstance;
    }

    public API_Routes getAPI(){
        return retrofit.create(API_Routes.class);
    }

}
