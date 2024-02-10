package com.udacity.asteroidradar.entity

import com.squareup.moshi.Json

data class CloseApproach(
    @Json(name = "relative_velocity")
    private val _relativeVelocity : RelativeVelocity,
    @Json(name = "miss_distance")
    private val _missedDistance : MissDistance
) {
    val relativeVelocity = _relativeVelocity.kilometersPerSecond
    val distanceFromEarth = _missedDistance.distanceFromEarth
}