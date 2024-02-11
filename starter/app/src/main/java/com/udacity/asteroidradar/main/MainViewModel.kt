package com.udacity.asteroidradar.main

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.NeoWsService
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.view.AsteroidAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repo: MainRepository) : ViewModel() {

    private val _selectedAsteroid = MutableSharedFlow<Asteroid>()

    val asteroidListClickListener = object : AsteroidAdapter.OnClickListener {
        override fun onClick(asteroid: Asteroid) {
            viewModelScope.launch {
                _selectedAsteroid.emit(asteroid)
            }
        }
    }

    private val _imageOfDayText = MutableLiveData(R.string.image_of_the_day_loading )
    val imageOfDayText = _imageOfDayText.map { it }

    init {
        refreshPictureOfDay()
        refreshAsteroids()
    }

    val selectedAsteroid = _selectedAsteroid.asSharedFlow()
    val pictureOfDay = repo.pictureOfDay.map { it }
    val asteroids = repo.asteroids.map { it }

    private val _progressBarVisibility = MutableLiveData(View.VISIBLE)
    val progressBarVisibility = _progressBarVisibility.map { it }

    private fun refreshAsteroids() {
        viewModelScope.launch {
            repo.refreshAsteroidsToday()
            _progressBarVisibility.value = View.GONE
        }
    }

    private fun refreshPictureOfDay() {
        viewModelScope.launch {
            repo.refreshImageOfDay()
            _imageOfDayText.value = R.string.image_of_the_day
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
            throw IllegalArgumentException("Unable to construct : ${modelClass.name} viewmodel")
        }
    }
}

