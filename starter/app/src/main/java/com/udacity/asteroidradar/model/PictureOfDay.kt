package com.udacity.asteroidradar.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.udacity.asteroidradar.R

data class PictureOfDay(
    val mediaType: String,
    val title: String,
    val url: String,
    @StringRes
    val doneLoadingDescriptionRes : Int = R.string.nasa_picture_of_day_content_description_format
)