package app.qup.util.common

import java.text.SimpleDateFormat
import java.util.Locale

fun millisToDateString(date: Long?, format: String = LOCAL_DATE_FORMAT): String {
    if (date == null || date == 0L) {
        return ""
    }
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(date)
}

fun String.onlyDigits(): Boolean = (firstOrNull { !it.isDigit() } == null)
