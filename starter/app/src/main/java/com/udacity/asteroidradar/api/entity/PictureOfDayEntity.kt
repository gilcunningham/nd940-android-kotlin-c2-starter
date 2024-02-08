package com.udacity.asteroidradar.api.entity

import com.squareup.moshi.Json

data class PictureOfDayEntity(
    @Json(name = "media_type")
    val mediaType: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "url")
    val url: String
)