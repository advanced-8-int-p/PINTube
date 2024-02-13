package com.example.pintube.utill

import android.content.Context
import android.content.Intent

object ShareLink {
    operator fun invoke(context: Context, videoUrl: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https:$videoUrl")
            type = "text/plain"
        }
        val shareChooser = Intent.createChooser(intent, null)
        context.startActivity(shareChooser)
    }
}