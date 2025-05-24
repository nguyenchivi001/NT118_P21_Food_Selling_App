package com.example.food_selling_app.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ConnectAI {
    private static final String BASE_URL = "https://api.together.xyz/";

    // Private constructor để ngăn chặn việc tạo instance từ bên ngoài
    private ConnectAI() {

    }
    // Lớp Holder nội bộ để thực hiện lazy initialization
    private static class ApiHolder {
        private static final TogetherApi INSTANCE = createInstance();

        private static TogetherApi createInstance() {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS) // Tăng thời gian chờ kết nối
                    .readTimeout(60, TimeUnit.SECONDS)    // Tăng thời gian chờ đọc
                    .writeTimeout(60, TimeUnit.SECONDS)   // Tăng thời gian chờ ghi
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client) // Thiết lập OkHttpClient tùy chỉnh
                    .build();

            return retrofit.create(TogetherApi.class);
        }
    }

    // Phương thức public static để lấy instance duy nhất của TogetherApi
    public static TogetherApi getInstance() {
        return ApiHolder.INSTANCE;
    }
}
