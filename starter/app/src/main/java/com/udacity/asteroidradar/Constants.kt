package com.udacity.asteroidradar

import java.util.Calendar

object Constants {
    const val API_QUERY_DATE_FORMAT = "yyyy-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    const val API_KEY = BuildConfig.NEO_WS_API_KEY
    const val START_OF_WEEK = Calendar.MONDAY
}