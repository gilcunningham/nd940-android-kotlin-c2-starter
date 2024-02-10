package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.view.AsteroidAdapter
import kotlinx.coroutines.launch

class MainViewModel(private val repo: MainRepository) : ViewModel() {

    val asteroidListClickListener = object : AsteroidAdapter.OnClickListener {
        override fun onClick(asteroid: Asteroid) {
            println("*** onCLick $asteroid")
        }
    }

    init {
        refreshPictureOfDay()
        refreshAsteroid()
    }

    val pictureOfDay: LiveData<PictureOfDay> = repo.pictureOfDay
    val asteroids: LiveData<List<Asteroid>> = repo.asteroids

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

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(
                    MainRepository(
                        AsteroidDatabase.getSome(app)
                    )
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

