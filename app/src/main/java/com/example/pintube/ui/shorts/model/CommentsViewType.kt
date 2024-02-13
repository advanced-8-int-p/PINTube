package com.example.pintube.ui.shorts.model

enum class CommentsViewType {
    ITEM,
    NO_ITEM,
    UNKNOWN
    ;

    companion object {
        fun from(ordinal: Int): CommentsViewType = CommentsViewType.values().find {
            it.ordinal == ordinal
        } ?: CommentsViewType.UNKNOWN
    }
}