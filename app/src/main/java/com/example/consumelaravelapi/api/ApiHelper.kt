package com.example.consumelaravelapi.api

import com.example.consumelaravelapi.entity.Comment
import com.example.consumelaravelapi.entity.Register
import com.example.consumelaravelapi.entity.Tutorial
import com.example.consumelaravelapi.entity.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiHelper {


    @POST("auth/signup")
    fun register(@Body register: Register): Call<ResponseBody>

    @FormUrlEncoded
    @POST("auth/signin")
    fun login(@Field("username") username: String, @Field("password") password: String): Call<User>


    @GET("tutorial")
    fun getData(): Call<List<Tutorial>>



    @GET("tutorial/{id}")
    fun getDetailData(@Path("id") id: String): Call<ResponseBody>


    @POST("comment/{id}")
    fun createComment(
        @HeaderMap headers: Map<String,String>,
        @Path("id") id: String,
        @Body body: Comment
    ): Call<ResponseBody>


    @POST("tutorial")
    fun createTutorial(
        @HeaderMap headers: Map<String,String>,
        @Body body: Tutorial
    ): Call<ResponseBody>

    @PUT("tutorial/{id}")
    fun updateTutorial (
        @HeaderMap headers: Map<String,String>,
        @Body body: Tutorial,
        @Path("id") id: String
    ): Call<ResponseBody>

    @DELETE("tutorial/{id}")
    fun deleteTutorial(
        @HeaderMap headers: Map<String,String>,
        @Path("id") id: String
    ):  Call<ResponseBody>

}