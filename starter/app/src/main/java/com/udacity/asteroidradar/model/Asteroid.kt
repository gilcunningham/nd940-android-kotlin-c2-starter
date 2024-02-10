package com.udacity.asteroidradar.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "asteroid_table")
@Parcelize
data class Asteroid(
    @PrimaryKey
    val id: Long,
    @ColumnInfo
    val codename: String,
    @ColumnInfo
    val closeApproachDate: String,
    @ColumnInfo
    val absoluteMagnitude: Double,
    @ColumnInfo
    val estimatedDiameter: Double,
    @ColumnInfo
    val relativeVelocity: Double,
    @ColumnInfo
    val distanceFromEarth: Double,
    @ColumnInfo
    val isPotentiallyHazardous: Boolean
) : Parcelable