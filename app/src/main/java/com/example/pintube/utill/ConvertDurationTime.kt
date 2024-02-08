package com.example.pintube.utill

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun String.convertDurationTime(): String {
    return run {
        val pattern = """PT(?:([0-9]+)H)?(?:([0-9]+)M)?(?:([0-9]+)S)?""".toRegex()
        val result = pattern.find(this)

        val hour = result?.groups?.get(1)?.value?.toIntOrNull() ?: 0
        val minute = result?.groups?.get(2)?.value?.toIntOrNull() ?: 0
        val seconds = result?.groups?.get(3)?.value?.toIntOrNull() ?: 0

        when (hour) {
            0 -> String.format("%02d:%02d", minute, seconds)
            else -> String.format("%02d:%02d:%02d", hour, minute, seconds)
        }
    }
}

fun String.convertPublishedAt(): String {
    return run {
        val originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.'Z'")
        val targetFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH-mm-ss")
        val dateTime = LocalDateTime.parse(this, originalFormatter)
        dateTime.format(targetFormatter)
    }
}
fun String.convertToDaysAgo() : String {
    val dateTime = this.let { ZonedDateTime.parse(it) }
    val localDateTime = dateTime?.toLocalDateTime()
    val now = LocalDateTime.now(ZoneOffset.UTC)
    val minutes = ChronoUnit.MINUTES.between(localDateTime, now)
    val hours = ChronoUnit.HOURS.between(localDateTime, now)
    val days = ChronoUnit.DAYS.between(localDateTime, now)
    val weeks = ChronoUnit.WEEKS.between(localDateTime, now)
    val months = ChronoUnit.MONTHS.between(localDateTime, now)
    val years = ChronoUnit.YEARS.between(localDateTime, now)

    return when {
        minutes.toInt() == 0 -> "방금전"
        minutes < 60 -> minutes.toString() + "분 전"
        hours < 24 -> hours.toString() + "시간 전"
        days < 14 -> days.toString() + "일 전"
        weeks < 4 -> weeks.toString() + "주 전"
        months < 12 -> months.toString() + "달 전"
        else -> years.toString() + "년 전"
    }
}

fun String.convertViewCount() : String {
    val count = this.toIntOrNull()
    return if (count != null) {
        when {
            count > 100000000 -> {
                String.format("%.1f", count / 100000000.0) + "억"
            }

            count > 100000 -> {
                String.format("%d", count / 10000) + "만"
            }
            count > 10000 -> {
                String.format("%.1f", count / 10000.0) + "만"
            }

            count > 1000 -> {
                String.format("%.1f", count / 1000.0) + "천"
            }

            else -> count.toString()
        }
    } else ""
}