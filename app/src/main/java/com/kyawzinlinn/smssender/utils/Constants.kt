package com.kyawzinlinn.smssender.utils

const val SMS_REQUEST_CODE = 20113

/* WORKER CONSTANTS */
const val KEY_PHONE_NUMBER = "KEY PHONE NUMBER"
const val KEY_MESSAGE = "KEY MESSAGE"
const val KEY_IS_TIME_OVER = "KEY IS TIME OVER"

/* NOTIFICATION CONSTANTS */
const val CHANNEL_ID = "10221"
const val REQUEST_CODE = 342
const val NOTIFICATION_ID = 92

enum class ScreenTitles (val title: String){
    REQUEST_PERMISSION("Allow Permission"),
    HOME("Home"),
    ADD("Add Message"),
    UPDATE("Update Message"),
    REPLY("Reply Messages"),
    ADD_REPLY_MESSAGE("Add Reply Message"),
    UPDATE_REPLY_MESSAGE("Update Reply Message")
}