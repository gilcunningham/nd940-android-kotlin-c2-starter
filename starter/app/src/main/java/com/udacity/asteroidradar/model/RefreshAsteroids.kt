package com.udacity.asteroidradar.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "refresh_table")
@Parcelize
data class RefreshAsteroids(
    @PrimaryKey
    val id: Int = 1,
    @ColumnInfo(name = "count")
    var count: Int
) : Parcelable