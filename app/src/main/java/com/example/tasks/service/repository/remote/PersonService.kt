package com.example.tasks.service.repository.remote

import com.example.tasks.service.model.HeaderModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PersonService {

    @POST("Authentication/Login")
    @FormUrlEncoded
    fun login(
        //O Field é o que passa dados no retrofit
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<HeaderModel>

    @POST("Authentication/Create")
    @FormUrlEncoded
    fun login(
        //O Field é o que passa dados no retrofit
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<HeaderModel>
}