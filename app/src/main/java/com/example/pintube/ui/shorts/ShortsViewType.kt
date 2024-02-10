package com.example.pintube.ui.shorts

enum class ShortsViewType {
    ITEM,
    UNKNOWN
    ;

    companion object {
        fun from(ordinal: Int): ShortsViewType = ShortsViewType.values().find {
            it.ordinal == ordinal
        } ?: UNKNOWN
    }
}