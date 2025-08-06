package com.example.screen_on_flutter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder

class ScreenService : Service() {
    var receiver: ScreenReceiver? = null;

    private val ANDROID_CHANNEL_ID = "screen_on_flutter"
    private val NOTIFICATION_ID = 9999
    fun LockScreen_registerReciver(){
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        receiver = ScreenReceiver()
        registerReceiver(receiver, filter)
        val intent = Intent(this,ScreenReceiver::class.java)
        this.sendBroadcast(intent)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent != null) {
            if (receiver == null) {
                LockScreen_registerReciver()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ANDROID_CHANNEL_ID,
                "screen_on_flutter",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notification = Notification.Builder(this, ANDROID_CHANNEL_ID)
            .setContentTitle("Mindit")
            .setContentText("Running in background")

            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .build()

        startForeground(NOTIFICATION_ID, notification)

        return Service.START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        if (receiver != null) {
            unregisterReceiver(receiver)
        }
    }
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}