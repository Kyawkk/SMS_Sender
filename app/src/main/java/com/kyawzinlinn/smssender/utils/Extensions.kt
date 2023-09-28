package com.kyawzinlinn.smssender.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toFormattedDateTime(): String{
    return this.format(DATE_TIME_FORMATTER)
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(): LocalDateTime{
    return LocalDateTime.parse(this, DATE_TIME_FORMATTER)
}