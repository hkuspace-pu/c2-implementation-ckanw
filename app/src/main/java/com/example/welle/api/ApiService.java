package com.example.welle.api;

import com.example.welle.data.remote.UserResponse;
import com.example.welle.data.remote.UserListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // 讀取特定使用者
    @GET("read_user/{student_id}/{user_id}")
    Call<UserResponse> getUser(
            @Path("student_id") String studentId,
            @Path("user_id") String userId
    );

    // 讀取所有使用者
    @GET("read_all_users/{student_id}")
    Call<UserListResponse> getAllUsers(
            @Path("student_id") String studentId
    );

    // 註冊新使用者
    @POST("create_user/{student_id}")
    Call<Void> createUser(
            @Path("student_id") String studentId,
            @Body UserResponse newUser
    );
}