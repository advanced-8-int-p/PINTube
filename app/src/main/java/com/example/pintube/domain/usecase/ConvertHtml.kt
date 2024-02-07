package com.example.pintube.domain.usecase

import android.text.Html
import android.text.Spanned

class ConvertHtml {
    operator fun invoke(text: String): Spanned {
        return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    }
}