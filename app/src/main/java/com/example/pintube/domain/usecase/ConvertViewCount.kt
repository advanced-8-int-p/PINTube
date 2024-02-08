package com.example.pintube.domain.usecase

class ConvertViewCount {
    operator fun invoke(viewCount: String?): String {
        val count = viewCount?.toIntOrNull()
        return if (count != null) {
            when {
                count > 100000000 -> {
                    String.format("%.1f", count / 100000000.0) + "억"
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
}