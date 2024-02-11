package com.udacity.asteroidradar.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.model.Asteroid

@JsonClass(generateAdapter = true)
data class AsteroidWrapperDto(
    @Json(name = "near_earth_objects")
    val nearEarthObjects: Map<String, List<AsteroidDto>>,
    @Json(name = "element_count")
    val elementCount: Int
)

fun AsteroidWrapperDto.asDomainModel(): List<Asteroid> {
    return nearEarthObjects.flatMap {
        it.value.asDomainModel(it.key)
    }
}