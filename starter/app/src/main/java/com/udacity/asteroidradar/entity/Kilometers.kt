package com.udacity.asteroidradar.entity

import com.squareup.moshi.Json

data class Kilometers(
    @Json(name = "estimated_diameter_max")
    val estimatedDiameterMax : Double
)