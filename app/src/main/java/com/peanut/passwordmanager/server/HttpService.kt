package com.peanut.passwordmanager.server

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.net.InetAddresses
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.util.Constants
import fi.iki.elonen.NanoHTTPD
import java.io.*
import java.net.*
import java.util.*


class HttpService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("q")
        HttpServer(Constants.NETWORK_PORT)
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(
                NotificationChannel("service", "本机ip:port", NotificationManager.IMPORTANCE_DEFAULT)
            )
        val customNotification = NotificationCompat.Builder(this, "service")
            .setSmallIcon(R.drawable.ic_splash_logo_dark)
            .setOngoing(true)
            .setContentTitle(
                "http://" + getAllLocalIpAddress().joinToString(
                    prefix = "{",
                    postfix = "}"
                ) + ":${Constants.NETWORK_PORT}"
            )
            .setContentText("请在局域网内设备的浏览器中输入,{}中任选一个")
            .setShowWhen(true)
            .build()
        notificationManager.notify((Math.random() * 10000).toInt(), customNotification)
        return super.onStartCommand(intent, flags, startId)
    }

    inner class HttpServer(HttpPort: Int) : NanoHTTPD(HttpPort) {
        init {
            start(SOCKET_READ_TIMEOUT, false)
        }

        override fun serve(session: IHTTPSession): Response {
            return try {
                return newFixedLengthResponse(SharedData.copiedData)
            } catch (e: Exception) {
                e.printStackTrace()
                newFixedLengthResponse(e.message)
            }
        }

    }

    private fun getAllLocalIpAddress(): Array<String> {
        var list = emptyArray<String>()
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf: NetworkInterface = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && !inetAddress.isLinkLocalAddress) {
                        list = list.plus(inetAddress.hostAddress.toString())
                    }
                }
            }
        } catch (ex: SocketException) {

        }
        return list
    }
}