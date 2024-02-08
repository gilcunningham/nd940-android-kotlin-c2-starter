package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NeoWsService
import com.udacity.asteroidradar.data.Asteroid
import com.udacity.asteroidradar.data.PictureOfDay
import java.text.SimpleDateFormat
import java.util.*

class MainRepository {

    private val _neoWsService = NeoWsService()
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()

    val pictureOfDay: LiveData<PictureOfDay> = _pictureOfDay

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids : LiveData<List<Asteroid>> = _asteroids

    suspend fun updateImageOfDay() {
        _neoWsService.fetchPictureOfDay()?.let {
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
        val startDate = formatter.format(startDate)
        val endDate = formatter.format(endDate)
        println("*** start = $startDate | end = $endDate")

        _asteroids.value = mutableListOf<Asteroid>().apply {
            _neoWsService.fetchAsteroidList(
                startDate, endDate
            )?.apply {
                nearEarthObjects.flatMap { entry ->
                    entry.value.map {
                        Asteroid(
                            it.id,
                            it.codename,
                            entry.key,
                            it.absoluteMagnitude,
                            it.estimatedDiameter.kilometers,
                            it.closeApproach.relativeVelocity,
                            it.closeApproach.distanceFromEarth,
                            it.isPotentiallyHazardous
                        )
                    }
                }.let {
                    addAll(it)
                }
            }
        }
    }
}