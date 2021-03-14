package com.skorik05.autodoctest.model.utils

fun parseDate(date: String?) : String {
    if (date == null) return ""
    return date.split("T")[0].split("-").joinToString(separator = ".")
}