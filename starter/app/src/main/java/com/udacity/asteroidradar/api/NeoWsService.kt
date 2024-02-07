package com.udacity.asteroidradar.api

import android.graphics.Picture
import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class NeoWsService {

    val API_KEY = ""

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttp = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .addNetworkInterceptor(interceptor)
        .build()

    private val moshiRetrofit = Retrofit.Builder()
        //.addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .client(okHttp)
        .build()

    private val neoWsApi : NeoWsApi by lazy {
        moshiRetrofit.create(NeoWsApi::class.java)
    }

    suspend fun fetchPictureOfDay() : PictureOfDay? {
        neoWsApi.fetchPictureOfDay(API_KEY).apply {
            return if (isSuccessful) {
                body()
            } else { null }
        }
    }

    interface NeoWsApi {

        //https://api.nasa.gov/neo/rest/v1/feed
        //
        //https://api.nasa.gov/planetary/apod
        @GET("/planetary/apod")
        suspend fun fetchPictureOfDay(
            @Query("api_key") apiKey : String
        ): Response<PictureOfDay>
    }
}