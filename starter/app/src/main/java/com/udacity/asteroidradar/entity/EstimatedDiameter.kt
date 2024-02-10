package com.udacity.asteroidradar.entity

import com.squareup.moshi.Json

data class EstimatedDiameter(
    @Json(name = "kilometers")
    private val _kilometers : Kilometers
) {
    val kilometers = _kilometers.estimatedDiameterMax
}