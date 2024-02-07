package com.example.pintube.domain.usecase

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConvertPublishedAt {
    operator fun invoke(date: String?): String {
        return if (date != null) {
            val originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.'Z'")
            val targetFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH-mm-ss")
            val dateTime = LocalDateTime.parse(date, originalFormatter)
            dateTime.format(targetFormatter)
        }else ""
    }
}