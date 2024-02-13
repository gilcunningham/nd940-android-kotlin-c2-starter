package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.START_OF_WEEK
import com.udacity.asteroidradar.api.NeoWsService
import com.udacity.asteroidradar.entity.asDomainModel
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MainRepository(private val database: AsteroidDatabase) {

    private val _filteredAsteroids = MutableSharedFlow<List<Asteroid>>()
    private val formatter
        get() = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    private val neoWsService = NeoWsService()
    private val _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val asteroids: LiveData<List<Asteroid>> = database.asteroidDao().getAsteroidListOrderByDate()
    val filteredAsteroids = _filteredAsteroids.asSharedFlow()
    val pictureOfDay: LiveData<PictureOfDay?> = _pictureOfDay
    val serviceStatus = neoWsService.serviceStatus

    suspend fun refreshImageOfDay() {
        _pictureOfDay.value = neoWsService.fetchPictureOfDay()?.asDomainModel()
    }

    suspend fun refreshAsteroidsToday() {
        val today = Date()
        updateAsteroidsByDateRange(today, today)
    }

    suspend fun fetchAsteroidsForNextNumberOfDays(numOfDays: Int) {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal.add(Calendar.DATE, -1)
        val yesterday = cal.time
        cal.add(Calendar.DATE, -numOfDays + 1)
        val nextNumberOfDaysDate = cal.time
        updateAsteroidsByDateRange(yesterday, nextNumberOfDaysDate)
    }

    suspend fun updateAsteroidsByDateRange(startDate: Date, endDate: Date) {
        val startDateFormatted = formatter.format(startDate)
        val endDateFormatted = formatter.format(endDate)
        neoWsService.fetchAsteroidList(
            startDateFormatted, endDateFormatted
        )?.let {
            database.asteroidDao().insertAll(it.asDomainModel())
        }
    }

    suspend fun filterTodaysAsteroids() {
        val cal = Calendar.getInstance(Locale.getDefault())
        filterAsteroidsByDate(cal.time)
    }

    suspend fun filterThisWeeksAsteroids() {
        val cal = Calendar.getInstance(Locale.getDefault())
        while (cal.get(Calendar.DAY_OF_WEEK) != START_OF_WEEK) {
            cal.add(Calendar.DATE, -1);
        }
        filterAsteroidsByDate(cal.time)
    }

    suspend fun filterAsteroidsByDate(endDate: Date) {
        val endDateFormatted = formatter.format(endDate)
        applyFilter(endDateFormatted)
    }

    suspend fun filterNoneAsteroids() {
        asteroids.value?.let { _filteredAsteroids.emit(it) }
    }

    private suspend fun applyFilter(endDateFormatted: String) =
        _filteredAsteroids.emit(
            mutableListOf<Asteroid>().apply {
                run breaking@{
                    asteroids.value?.forEach {
                        if (endDateFormatted <= it.closeApproachDate) { add(it) }
                        else { return@breaking }
                    }
                }
            }
        )
}

