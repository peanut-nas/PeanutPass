package com.peanut.passwordmanager.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyAutofillService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}