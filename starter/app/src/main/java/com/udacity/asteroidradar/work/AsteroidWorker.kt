package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Constants.DEFAULT_END_DATE_DAYS
import com.udacity.asteroidradar.main.AsteroidDatabase
import com.udacity.asteroidradar.main.MainRepository
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

fun setupWorkers(applicationContext: Context) {
    CoroutineScope(Dispatchers.Default).launch {
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
        return try {
            Timber.d("do work")
            val database = AsteroidDatabase.getInstance(applicationContext)
            MainRepository(database).fetchAsteroidsForNextNumberOfDays(DEFAULT_END_DATE_DAYS)
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshAsteroidWorker"
    }
}