package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.api.entity.AsteroidListWrapper
import com.udacity.asteroidradar.api.entity.PictureOfDayEntity
import com.udacity.asteroidradar.data.Asteroid
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class NeoWsService {

    enum class Status { LOADING, ERROR, DONE }

    private val _serviceStatus = MutableSharedFlow<Status>()
    val serviceStatus = _serviceStatus.asSharedFlow()

    val API_KEY = BuildConfig.NEO_WS_API_KEY

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
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .client(okHttp)
        .build()

    private val neoWsApi: NeoWsApi by lazy {
        moshiRetrofit.create(NeoWsApi::class.java)
    }

    suspend fun fetchPictureOfDay(): PictureOfDayEntity? {
        return serviceCall {
            neoWsApi.fetchPictureOfDay(API_KEY)
        }
    }

    suspend fun fetchAsteroidList(
        formattedStartDate: String,
        formattedEndDate: String
    ): AsteroidListWrapper? {
        return serviceCall {
            neoWsApi.fetchAsteroidsByDateRange(
                API_KEY,
                formattedStartDate,
                formattedEndDate
            )
        }
    }

    suspend fun <T : Any> serviceCall(
        serviceDelegate: suspend () -> Response<T>
    ): T? = withContext(Dispatchers.Default) {
        _serviceStatus.emit(Status.LOADING)
        serviceDelegate().apply {
            _serviceStatus.emit(if (isSuccessful) Status.DONE else Status.ERROR)
        }.body()
        //    apply {
        //    _serviceStatus.emit(if (isSuccessful) Status.DONE else Status.ERROR)
        //}.body()
    }

    interface NeoWsApi {

        //https://api.nasa.gov/neo/rest/v1/feed
        //
        //https://api.nasa.gov/planetary/apod
        @GET("/planetary/apod")
        suspend fun fetchPictureOfDay(
            @Query("api_key") apiKey: String
        ): Response<PictureOfDayEntity>

        @GET("/neo/rest/v1/feed")
        suspend fun fetchAsteroidsByDateRange(
            @Query("api_key") apiKey: String,
            @Query("start_date") formattedStartDate: String,
            @Query("end_date") formattedEndDate: String,
        ): Response<AsteroidListWrapper>
        //https://api.nasa.gov/neo/rest/v1/feed?start_date=2015-09-07&end_date=2015-09-08&api_key=DEMO_KEY
    }
}