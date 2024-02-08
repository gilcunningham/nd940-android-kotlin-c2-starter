package com.udacity.asteroidradar.api.entity

import com.squareup.moshi.Json

data class AsteroidEntity(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val codename: String,
    @Json(name = "close_approach_data")
    private val _closeApproach: List<CloseApproach>,
    @Json(name = "absolute_magnitude_h")
    val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter")
    val estimatedDiameter: EstimatedDiameter,
    @Json(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean
) {
    val closeApproach = _closeApproach[0]
}