package com.example.pintube.ui.home

/** 1 */
const val MULTI_POPULAR = 1

/** 2 */
const val MULTI_CATEGORY = 2

data class MultiData(
    val image: Int = -1,
    val name: String = "",
    val age: Int = -1,
    val type: Int = -1,
)
