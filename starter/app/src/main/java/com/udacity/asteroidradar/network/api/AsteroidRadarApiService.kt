package com.udacity.asteroidradar.network.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.domain.PictureOfDay
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create()) //retrofit knows about how to convert json to string
        .addConverterFactory(MoshiConverterFactory.create(moshi)) //retrofit knows about how to convert json to moshi
        .baseUrl(BASE_URL).build()

interface AsteroidRadarApiService {
    @GET("/planetary/apod?api_key=${API_KEY}")
    suspend fun getImage(): PictureOfDay?

    @GET("/neo/rest/v1/feed")
    suspend fun getAsteroid(
            @Query("api_key")
            apiKey: String = API_KEY,
            @Query("start_date")
            startDate: String,
            @Query("end_date")
            endDate: String
    ): Response<String>
}

object AsteroidApi {
    val retrofitService: AsteroidRadarApiService by lazy {
        retrofit.create(AsteroidRadarApiService::class.java)
    }
}