package com.udacity.asteroidradar.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.udacity.asteroidradar.model.Asteroid

@Entity
data class AsteroidEntity(
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

fun List<AsteroidEntity>.asDomainModel(closeApproachDate: String): List<Asteroid> {
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