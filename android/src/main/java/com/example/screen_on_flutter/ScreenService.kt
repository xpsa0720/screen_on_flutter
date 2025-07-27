package com.example.screen_on_flutter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import io.flutter.Log

class ScreenService : Service() {

    var receiver: ScreenReceiver? = null;
    private val ANDROID_CHANNEL_ID = "familylovenotification"
    private val NOTIFICATION_ID = 9999
    fun LockScreen_registerReciver(name:String?){
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        receiver = ScreenReceiver()
        registerReceiver(receiver, filter)

        val intent = Intent(this,ScreenReceiver::class.java)
        intent.action = "entryPoint"
        intent.putExtra("entryPointName",name)
        this.sendBroadcast(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent != null) {
            if (receiver == null) {
                LockScreen_registerReciver(intent.getStringExtra("entryPointName"))
            }
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(ANDROID_CHANNEL_ID, "LockScreen", NotificationManager.IMPORTANCE_HIGH)
            val notificationManger = getSystemService(NotificationManager::class.java)
            notificationManger.createNotificationChannel(channel)

        }

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, ANDROID_CHANNEL_ID).setContentTitle("넌 춘식이여")
                .setContentText("알겠지?").setSmallIcon(android.R.drawable.ic_lock_idle_lock)
        } else {
            Notification.Builder(this)
                .setContentTitle("알림 시작했어")
                .setContentText("시발")
                .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
        }
        val notification = builder.build()

        startForeground(NOTIFICATION_ID, builder.build())


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