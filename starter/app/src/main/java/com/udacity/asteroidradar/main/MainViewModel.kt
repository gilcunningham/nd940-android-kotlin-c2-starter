package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.entity.PictureOfDayEntity
import com.udacity.asteroidradar.data.Asteroid
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repo = MainRepository()

    init {
        refreshPictureOfDay()
        refreshAsteroid()
    }

    val pictureOfDay : LiveData<PictureOfDayEntity> = repo.pictureOfDay
    val asteroid : LiveData<List<Asteroid>> = repo.asteroids

    private fun refreshPictureOfDay() {
        viewModelScope.launch {
            repo.updateImageOfDay()
        }
    }

    fun refreshAsteroid() {
        viewModelScope.launch {
            repo.updateAsteroidsPast24Hrs()
        }
    }
}