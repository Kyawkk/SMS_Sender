package com.kyawzinlinn.smssender.utils

data class FormFieldState(
    var keyword: String = "",
    var reply: String = "",
    var isKeywordValid : Boolean = true,
    var isReplyValid : Boolean = true,
    val errorMessage: String = ""
)