package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.Asteroid
import com.udacity.asteroidradar.data.PictureOfDay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val asteroidListClickListener = object : AsteroidAdapter.OnClickListener {
        override fun onClick(asteroid: Asteroid) {
            println("*** onCLick $asteroid")
        }
    }

    private val repo = MainRepository()

    init {
        refreshPictureOfDay()
        refreshAsteroid()
    }

    val pictureOfDay : LiveData<PictureOfDay> = repo.pictureOfDay
    val asteroids : LiveData<List<Asteroid>> = repo.asteroids

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