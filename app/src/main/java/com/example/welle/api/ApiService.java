package com.example.welle.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("login")   // 假設你的伺服器端點是 /login
    Call<UserResponse> loginUser(
            @Field("email") String email,
            @Field("mobile") String mobile
    );
}
