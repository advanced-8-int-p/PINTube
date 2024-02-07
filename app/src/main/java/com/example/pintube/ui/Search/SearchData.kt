package com.example.pintube.ui.Search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

data class SearchData(
    val timestamp: String,
    val term: String
)

