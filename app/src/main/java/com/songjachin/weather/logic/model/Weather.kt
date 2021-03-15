package com.songjachin.weather.logic.model

import com.songjachin.weather.logic.model.DailyResponse.Daily
import com.songjachin.weather.logic.model.RealtimeResponse.Realtime

/**
 * Created by matthew
 *
 *
 */
data class Weather(val realtime: Realtime, val daily: Daily)