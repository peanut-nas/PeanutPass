package com.peanut.passwordmanager.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.server.SharedData
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

object AdditionalFunctions {

    fun String.copy(context: Context) {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("com.peanut.pm", this)
        clipboard?.setPrimaryClip(clip)
        SharedData.copiedData = this
    }

    fun isPortAvailable(port: Int): Boolean {
        return try {
            val s = Socket()
            s.bind(InetSocketAddress("127.0.0.1", port))
            s.close()
            true
        } catch (e: Exception) {
            false
        }
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

    private fun copyFileUseStream(
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

    fun Account.toJsonString(): String{
        val jsonObject = JSONObject().apply {
            this.put("id", this@toJsonString.id)
            this.put("title", this@toJsonString.title)
            this.put("icon", this@toJsonString.icon)
            this.put("account", this@toJsonString.account)
            this.put("password", this@toJsonString.password)
            this.put("accountType", this@toJsonString.accountType.name)
            this.put("accessTime", this@toJsonString.accessTime)
            this.put("createTime", this@toJsonString.createTime)
            this.put("accessTimes", this@toJsonString.accessTimes)
        }
        return jsonObject.toString(4)
    }

    fun String.buildToAccount(): Account{
        val jsonObject = JSONObject(this)
        return Account(
            id = jsonObject.getInt("id"),
            title = jsonObject.getString("title"),
            icon = jsonObject.getString("icon"),
            account = jsonObject.getString("account"),
            password = jsonObject.getString("password"),
            accountType = AccountType.valueOf(jsonObject.getString("accountType")),
            accessTime = jsonObject.getLong("accessTime"),
            createTime = jsonObject.getLong("createTime"),
            accessTimes = jsonObject.getInt("accessTimes"),
        )
    }
}