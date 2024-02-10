package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NeoWsService
import com.udacity.asteroidradar.entity.asDomainModel
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(private val database: AsteroidDatabase) {

    private val neoWsService = NeoWsService()
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    private val asteroidDao = database.asteroidDao()

    val pictureOfDay: LiveData<PictureOfDay> = _pictureOfDay

    val asteroids : LiveData<List<Asteroid>> = database.asteroidDao().getAsteroidListOrderByData()
        //database.asteroidDao().getAsteroidListOrderByData()

    suspend fun updateImageOfDay() {

        neoWsService.fetchPictureOfDay()?.let {
            _pictureOfDay.value = PictureOfDay(
                it.mediaType,
                it.title,
                it.url
            )
        }

    }

    suspend fun updateAsteroidsPast24Hrs() {
        val today = Calendar.getInstance()
        val endDate = today.time
        val startDate = today.apply { add(Calendar.DAY_OF_YEAR, -1) }.time
        updateAsteroidsByDateRange(startDate, endDate)
    }

    suspend fun updateAsteroidsByDateRange(startDate: Date, endDate: Date) {
        val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val startDateFormatted = formatter.format(startDate)
        val endDateFormatted = formatter.format(endDate)
        println("*** start = $startDateFormatted | end = $endDateFormatted")
        println("*** asteroids : ${asteroids.value}")




            val asteroidsWrapper = neoWsService.fetchAsteroidList(
                startDateFormatted, endDateFormatted
            )
            println("*** fetched =  ${asteroidsWrapper?.asDomainModel()}")


            asteroidsWrapper?.let {
                database.asteroidDao().insertAll(listOf())
            //database.asteroidDao().insertAll(asteroidsWrapper.asDomainModel())
            }


    }
}
