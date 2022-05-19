package com.peanut.passwordmanager.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object AdditionalFunctions {
    fun String.copy(context: Context) {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("com.peanut.pm", this)
        clipboard?.setPrimaryClip(clip)
    }
}