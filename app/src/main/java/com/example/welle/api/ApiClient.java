package com.example.welle.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // ⚠️ 請替換成你的 API Base URL，例如：
    // "http://10.240.72.69/comp2000/coursework/"
    private static final String BASE_URL = "http://10.240.72.69/comp2000/coursework/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // 使用 Gson 解析 JSON
                    .build();
        }
        return retrofit;
    }
}