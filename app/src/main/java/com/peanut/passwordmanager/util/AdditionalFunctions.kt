package com.peanut.passwordmanager.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object AdditionalFunctions {

    fun String.copy(context: Context) {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("com.peanut.pm", this)
        clipboard?.setPrimaryClip(clip)
    }

    fun copyFile(source: Uri, destination: String, context: Context) {
        source.let { returnUri ->
            when (returnUri.scheme) {
                "file" -> {
                    val fis = FileInputStream(returnUri.toFile())
                    val fos = FileOutputStream(destination)
                    copyFileUseStream(fis, fos)
                    fis.close()
                    fos.apply { this.flush() }.close()
                    return
                }
                "content" -> {
                    val sharedDB = context.contentResolver.openFileDescriptor(source, "r")
                    sharedDB?.let {
                        val fd = it.fileDescriptor
                        val fis = FileInputStream(fd)
                        val fos = FileOutputStream(destination)
                        copyFileUseStream(fis, fos)
                        fis.close()
                        fos.apply { this.flush() }.close()
                        return
                    }
                }
                else -> {}
            }
        }
    }

    fun copyFileUseStream(
        fileInputStream: InputStream,
        fileOutputStream: OutputStream,
        close: Boolean = true
    ) {
        try {
            val buffer = ByteArray(1024)
            var byteRead: Int
            while (-1 != fileInputStream.read(buffer).also { byteRead = it }) {
                fileOutputStream.write(buffer, 0, byteRead)
            }
            fileOutputStream.flush()
            if (close) {
                fileInputStream.close()
                fileOutputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}