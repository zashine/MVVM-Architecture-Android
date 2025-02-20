package me.amitshekhar.mvvm.ui.flow

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface MyApi {

    @GET("/comments")
    fun getComments(): Call<List<Comment>>

    @GET("/comments")
    suspend fun getComments2(): Response<List<Comment>>
}