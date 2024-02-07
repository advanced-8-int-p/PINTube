package com.example.pintube.domain.usecase

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class ConvertToDaysAgo {
    operator fun invoke(date: String?): String {
        if (date != null) {
            val dateTime = date.let { ZonedDateTime.parse(it) }
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
        }else return ""
    }
}

