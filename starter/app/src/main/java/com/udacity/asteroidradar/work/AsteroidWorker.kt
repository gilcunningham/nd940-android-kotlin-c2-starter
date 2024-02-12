package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.main.AsteroidDatabase
import com.udacity.asteroidradar.main.MainRepository
import com.udacity.asteroidradar.model.RefreshAsteroids
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

private val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.UNMETERED)
    .setRequiresCharging(true)
    .build()

private val repeatingRequest =
    PeriodicWorkRequestBuilder<RefreshAsteroidWorker>(1, TimeUnit.DAYS)
        .setConstraints(constraints)
        .build()

private suspend fun initializeRefreshAsteroids(applicationContext: Context) {
    val database = AsteroidDatabase.getInstance(applicationContext)
    val refreshDao = database.refreshAsteroidsDao()
    refreshDao.insert(RefreshAsteroids(count = 0))
}

fun setupWorkers(applicationContext: Context) {
    CoroutineScope(Dispatchers.Default).launch {
        initializeRefreshAsteroids(applicationContext)
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshAsteroidWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            repeatingRequest
        )
    }
}

class RefreshAsteroidWorker(
    context: Context, userParameters: WorkerParameters
) : CoroutineWorker(context, userParameters) {
    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val refreshAsteroidsDao = database.refreshAsteroidsDao()
        val refreshAsteroids = refreshAsteroidsDao.getRefreshAsteroids()
        Timber.d("refresh count: ${refreshAsteroids.count}")

        if (Constants.DEFAULT_END_DATE_DAYS <= refreshAsteroids.count) {
            Timber.d("refresh count exceeded ${Constants.DEFAULT_END_DATE_DAYS}, cancelling work")
            WorkManager.getInstance(applicationContext).cancelUniqueWork(WORK_NAME)
            return Result.success()
        }

        return try {
            Timber.d("do work")
            MainRepository(database).refreshAsteroidsToday()
            refreshAsteroidsDao.insert(
                refreshAsteroids.apply { count++ }
            )
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshAsteroidWorker"
    }
}