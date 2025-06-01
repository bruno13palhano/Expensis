package com.bruno13palhano.expensis.ui.shared

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import java.time.LocalDateTime
import java.time.ZoneOffset

val dateFormat: DateFormat = SimpleDateFormat.getDateInstance().apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

fun currentDate() = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

fun currentTimestamp(): String = dateFormat.format(currentDate())
