package com.kyawzinlinn.smssender.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.google.gson.Gson
import com.kyawzinlinn.smssender.model.MessageDto
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy,hh:mm a")

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

fun String.toFormattedTime(): String{
    return try {
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(this)
    }catch (e: Exception) {
        this
    }
}

fun String.toMessageObject(): MessageDto{
    val formattedString = this
        .replace("MessageDto(","{")
        .replace("{", "{\"")
        .replace("}", "\"}")
        .replace(", ", "\", \"")
        .replace("=", "\":\"")
        .replace("true", "true")
        .replace("false", "false")
        .replace(")","\"}")
    val gson = Gson()
    val messageDTO = gson.fromJson(formattedString,MessageDto::class.java)
    return if (messageDTO.delayTime.trim().isNullOrEmpty()) messageDTO.copy(delayTime = "Date,Time")
    else messageDTO
}