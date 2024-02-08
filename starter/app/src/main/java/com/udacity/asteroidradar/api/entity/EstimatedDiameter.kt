package com.udacity.asteroidradar.api.entity

import com.squareup.moshi.Json

data class EstimatedDiameter(
    @Json(name = "kilometers")
    private val _kilometers : Kilometers
) {
    val kilometers = _kilometers.estimatedDiameterMax
}