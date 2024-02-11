package com.udacity.asteroidradar.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.udacity.asteroidradar.model.Asteroid

@Entity
data class AsteroidDto(
    @Json(name = "id")
    @PrimaryKey
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

fun List<AsteroidDto>.asDomainModel(closeApproachDate: String): List<Asteroid> {
    return map {
        Asteroid(
            it.id,
            it.codename,
            closeApproachDate,
            it.absoluteMagnitude,
            it.estimatedDiameter.kilometers,
            it.closeApproach.relativeVelocity,
            it.closeApproach.distanceFromEarth,
            it.isPotentiallyHazardous
        )
    }
}

data class CloseApproach(
    @Json(name = "relative_velocity")
    private val _relativeVelocity : RelativeVelocity,
    @Json(name = "miss_distance")
    private val _missedDistance : MissDistance
) {
    val relativeVelocity = _relativeVelocity.kilometersPerSecond
    val distanceFromEarth = _missedDistance.distanceFromEarth
}

data class EstimatedDiameter(
    @Json(name = "kilometers")
    private val _kilometers : Kilometers
) {
    val kilometers = _kilometers.estimatedDiameterMax
}

data class Kilometers(
    @Json(name = "estimated_diameter_max")
    val estimatedDiameterMax : Double
)

data class MissDistance(
    @Json(name = "astronomical")
    val distanceFromEarth : Double
)

data class RelativeVelocity(
    @Json(name = "kilometers_per_second")
    val kilometersPerSecond : Double
)