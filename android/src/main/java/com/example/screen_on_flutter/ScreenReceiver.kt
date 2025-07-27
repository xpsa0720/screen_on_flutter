package com.example.screen_on_flutter

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.WindowManager
import androidx.annotation.RequiresPermission
import io.flutter.embedding.android.FlutterActivity

class ScreenReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == "entryPoint"){
            val entryPointName = intent.getStringExtra("entryPointName")
            Log.e("액트리 포인터 이름",entryPointName.toString())
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("entryPointName", entryPointName)
                .apply()
        }

        if (intent.action == Intent.ACTION_SCREEN_ON) {
            val entryPoint = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                .getString("entryPointName", "main2")
            if(entryPoint == null){return}
            ScreenOn(context, entryPoint)
        }
    }

    private fun ScreenOn(context: Context, name:String){
        try {
            val i = FlutterActivity.withCachedEngine("LockScreenEngine_id").build(context).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                .NewEngineIntentBuilder(LockScreenActivity::class.java).dartEntrypointArgs()

            context.startActivity(i)
        } catch (e: Exception) {
            Log.e("에러!!!!!!", e.toString())

        }
    }


}