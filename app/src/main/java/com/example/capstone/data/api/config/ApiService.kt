package com.example.capstone.data.api.config

import com.example.capstone.data.api.response.LoginResponse
import com.example.capstone.data.api.response.LogoutResponse
import com.example.capstone.data.api.response.ProfileResponse
import com.example.capstone.data.api.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("fullName") name: String,
        @Field("heightCm") height: Float,
        @Field("weightKg") weight : Float,
        @Field("ageYears") age : Int,
        @Field("armCircumferenceCm") circleHand : Float
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token: String,
    ): LogoutResponse

    @FormUrlEncoded
    @POST("profile")
    suspend fun profile(
        @Header("Authorization") token: String,
        @Field("fullName") name: String,
        @Field("weightKg") weight : Float,
        @Field("ageYears") age : Int,
        @Field("armCircumferenceCm") circleHand : Float,
        @Field("heightCm") height: Float
    ): ProfileResponse
}