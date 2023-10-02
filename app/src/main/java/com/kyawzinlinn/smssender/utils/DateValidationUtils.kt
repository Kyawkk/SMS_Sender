package com.kyawzinlinn.smssender.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateValidationUtils {
    const val DATE_FORMAT = "dd-MM-yyyy"
    const val TIME_FORMAT = "HH:mm a"
    fun getTodayDate(): String{
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT)
        return simpleDateFormat.format(calendar.time)
    }
    fun validateDate(date: String): Boolean{
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return try {
            simpleDateFormat.parse(date)
            true
        }catch (e: Exception) {
            false
        }
    }

    fun validateTime(time: String): Boolean{
        val simpleDateFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        return try {
            simpleDateFormat.parse(time)
            true
        }catch (e: Exception) {
            false
        }
    }
}