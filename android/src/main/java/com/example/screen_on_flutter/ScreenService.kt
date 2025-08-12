package com.example.screen_on_flutter

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import io.flutter.FlutterInjector
import io.flutter.Log

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

        try {
            val alarmModel =intent?.getParcelableExtra<AlarmModel>("alarmModel")

            if (receiver == null) {
                LockScreen_registerReciver()
            }

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    ANDROID_CHANNEL_ID,
                    "screen_on_view",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    setShowBadge(false)
                }
                channel.setShowBadge(false)
                manager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(this, ANDROID_CHANNEL_ID)
                .setContentTitle(alarmModel?.title?:"screen_on_view")
                .setContentText(alarmModel?.content?:"background running")
                .setOngoing(true)
                .setNumber(0)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)


            startForeground(NOTIFICATION_ID, notification.build())
            return START_REDELIVER_INTENT

        }catch(e: Exception){
            Log.e("Screen_on_flutter Error",e.toString())
            return START_NOT_STICKY
        }
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