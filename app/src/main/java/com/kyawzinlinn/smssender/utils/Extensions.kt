package com.kyawzinlinn.smssender.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")

@RequiresApi(Build.VERSION_CODES.O)
fun String.toFormattedDateTime(): String{
    return this.format(DATE_TIME_FORMATTER)
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDateTime(): LocalDateTime{
    return LocalDateTime.parse(this, DATE_TIME_FORMATTER)
}


@ExperimentalMaterial3Api
fun TimePickerState.toFormattedTime(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, this.hour)
    calendar.set(Calendar.MINUTE, this.minute)
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time)
}