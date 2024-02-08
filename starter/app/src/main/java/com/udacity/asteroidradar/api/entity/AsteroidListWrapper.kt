package com.udacity.asteroidradar.api.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AsteroidListWrapper(
    @Json(name = "near_earth_objects")
    val nearEarthObjects: Map<String, List<AsteroidEntity>>,
    @Json(name = "element_count")
    val elementCount : Int
)