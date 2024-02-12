package com.udacity.asteroidradar.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.RefreshAsteroids

@Database(entities = [Asteroid::class, RefreshAsteroids::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract fun asteroidDao() : AsteroidDao

    abstract fun refreshAsteroidsDao() : RefreshAsteroidsDao

    companion object {
        private const val DATABASE_NAME = "asteroids-db"
        private lateinit var INSTANCE: AsteroidDatabase
        fun getInstance(context: Context): AsteroidDatabase {
            synchronized(AsteroidDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AsteroidDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroid_table ORDER by closeApproachDate DESC")
    fun getAsteroidListOrderByDate(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<Asteroid>)
}

@Dao
interface RefreshAsteroidsDao {
    @Query("SELECT * FROM refresh_table")
    fun getRefreshAsteroids() : RefreshAsteroids

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(refreshAsteroid: RefreshAsteroids)
}



