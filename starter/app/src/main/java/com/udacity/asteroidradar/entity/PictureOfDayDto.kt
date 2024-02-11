package com.udacity.asteroidradar.entity

import com.squareup.moshi.Json
import com.udacity.asteroidradar.model.PictureOfDay

data class PictureOfDayDto(
    @Json(name = "media_type")
    val mediaType: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "url")
    val url: String
)

fun PictureOfDayDto.asDomainModel(): PictureOfDay {
    return PictureOfDay(mediaType, title, url)
}