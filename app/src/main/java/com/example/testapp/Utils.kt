package com.example.testapp

import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

// Функция для форматирования размера файла в удобный вид
fun bytesToHumanReadableSize(bytes: Double) = when {
    bytes >= 1 shl 30 -> "%.1f GB".format(bytes / (1 shl 30))
    bytes >= 1 shl 20 -> "%.1f MB".format(bytes / (1 shl 20))
    bytes >= 1 shl 10 -> "%.0f kB".format(bytes / (1 shl 10))
    else -> "$bytes B"
}

// Форматируем дату в нормальный вид
fun formatDate(dateString: String): String {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val zonedDateTime = ZonedDateTime.parse(dateString, formatter)

    val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).format(outputFormatter)
}