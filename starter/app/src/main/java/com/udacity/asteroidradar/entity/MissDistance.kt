package com.udacity.asteroidradar.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MissDistance(
    @Json(name = "astronomical")
    val distanceFromEarth : Double
) : Parcelable