package com.example.screen_on_flutter

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.screen_on_flutter.ScreenOnFlutterPlugin.EventChannelBridge

class ScreenReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_ON) {
            ScreenOn(context)
            EventChannelBridge.eventChannelHandler?.sink("SCREEN_ON");
        }
    }


    private fun ScreenOn(context: Context){
        try {
            val i = CustomFlutter.withCachedEngine("screen_on_flutter").build(context).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        } catch (e: Exception) {
            Log.e("ScreenOn 에러!!!!!!", e.toString())
        }
    }

}