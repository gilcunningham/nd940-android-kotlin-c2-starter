package com.udacity.asteroidradar.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.model.Asteroid

@JsonClass(generateAdapter = true)
data class AsteroidListWrapper(
    @Json(name = "near_earth_objects")
    val nearEarthObjects: Map<String, List<AsteroidEntity>>,
    @Json(name = "element_count")
    val elementCount: Int
)

fun AsteroidListWrapper.asDomainModel(): List<Asteroid> {
    return nearEarthObjects.flatMap {
        it.value.asDomainModel(it.key)
    }
}

    //return nearEarthObjects.flatMap {
    //    it.value.asDomainModel(it.key)
    //}
//}
//return foo
/**
Asteroid(
it.id,
it.codename,
entry.key,
it.absoluteMagnitude,
it.estimatedDiameter.kilometers,
it.closeApproach.relativeVelocity,
it.closeApproach.distanceFromEarth,
it.isPotentiallyHazardous
)
 **/


/**
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
 **/


/**
nearEarthObjects.flatMap { entry ->
entry.value.map { it }

Asteroid(
it.id,
it.codename,
entry.key,
it.absoluteMagnitude,
it.estimatedDiameter.kilometers,
it.closeApproach.relativeVelocity,
it.closeApproach.distanceFromEarth,
it.isPotentiallyHazardous
)
}
 **/
