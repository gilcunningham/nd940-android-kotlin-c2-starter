package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NeoWsService
import com.udacity.asteroidradar.entity.asDomainModel
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainRepository(private val database: AsteroidDatabase) {

    private val neoWsService = NeoWsService()
    private val _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val asteroids: LiveData<List<Asteroid>> = database.asteroidDao().getAsteroidListOrderByDate()
    val pictureOfDay: LiveData<PictureOfDay?> = _pictureOfDay
    val serviceStatus = neoWsService.serviceStatus

    suspend fun refreshImageOfDay() {
        _pictureOfDay.value = neoWsService.fetchPictureOfDay()?.asDomainModel()
    }

    suspend fun refreshAsteroidsToday() {
        val today = Date()
        updateAsteroidsByDateRange(today, today)
    }

    suspend fun updateAsteroidsByDateRange(startDate: Date, endDate: Date) {
        val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val startDateFormatted = formatter.format(startDate)
        val endDateFormatted = formatter.format(endDate)

        neoWsService.fetchAsteroidList(
            startDateFormatted, endDateFormatted
        )?.let {
            database.asteroidDao().insertAll(it.asDomainModel())
        }
    }
}
