package com.example.pintube.domain.usecase

class ConvertDurationTime {
    operator fun invoke(duration: String?): String {
        return if (duration != null) {
            val pattern = """PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?.""".toRegex()
            val result = pattern.find(duration)

            val hour = result?.groups?.get(1)?.value?.toIntOrNull() ?: 0
            val minute = result?.groups?.get(2)?.value?.toIntOrNull() ?: 0
            val seconds = result?.groups?.get(3)?.value?.toIntOrNull() ?: 0

            when (hour) {
                0 -> String.format("%02d:%02d", minute, seconds)
                else -> String.format("%02d:%02d:%02d", hour, minute, seconds)
            }
        }else ""
    }
}