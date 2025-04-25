package com.example.food_selling_app.api;

import androidx.annotation.Nullable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    public static Retrofit getClient(@Nullable String token) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if (token != null && !token.isEmpty()) {
            clientBuilder.addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            });
        }

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();
    }
}