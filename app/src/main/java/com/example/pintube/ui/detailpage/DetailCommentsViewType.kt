package com.example.pintube.ui.detailpage

enum class DetailCommentsViewType {
    ITEM,
    NO_ITEM,
    UNKNOWN
    ;

    companion object {
        fun from(ordinal: Int): DetailCommentsViewType = DetailCommentsViewType.values().find {
            it.ordinal == ordinal
        } ?: DetailCommentsViewType.UNKNOWN
    }
}