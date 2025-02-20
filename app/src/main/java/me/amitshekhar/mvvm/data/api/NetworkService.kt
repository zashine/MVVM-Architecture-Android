package me.amitshekhar.mvvm.data.api

import me.amitshekhar.mvvm.data.model.TopHeadlinesResponse
import me.amitshekhar.mvvm.utils.AppConstant
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import javax.inject.Singleton
import retrofit2.Response

@Singleton
interface NetworkService {

    @Headers("X-Api-Key: ${AppConstant.API_KEY}")
    @GET("top-headlines")
    suspend fun getTopHeadlines(@Query("country") country: String): TopHeadlinesResponse

    @GET("top-headlines")
    suspend fun getTopHeadlines2(
        @Query("language") language: String = AppConstant.LANGUAGE,
        @Query("apikey") apikey: String = AppConstant.API_KEY,
        @Query("country") country: String = AppConstant.COUNTRY
    ): TopHeadlinesResponse

    @GET("top-headlines")
    suspend fun getTopHeadlines3(
        @Query("language") language: String = AppConstant.LANGUAGE,
        @Query("apikey") apikey: String = AppConstant.API_KEY,
        @Query("country") country: String = AppConstant.COUNTRY
    ): Response<TopHeadlinesResponse>
}