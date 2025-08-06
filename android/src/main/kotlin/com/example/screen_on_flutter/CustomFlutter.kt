package com.example.screen_on_flutter
import android.content.Context
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache

class CustomFlutter: FlutterActivity() {
    override fun onDestroy() {
        ScreenOnFlutterPlugin.mainActivityFinishAffinity()
        super.onDestroy()
    }

    override fun onUserLeaveHint(){
        ScreenOnFlutterPlugin.mainActivityFinishAffinity()
        super.onUserLeaveHint()
    }

    override fun provideFlutterEngine(context: Context): FlutterEngine? {
        return FlutterEngineCache.getInstance().get("screen_on_flutter")
    }

    companion object{
        fun withCachedEngine(cachedEngineId: String): CachedEngineIntentBuilder {
            return CachedEngineIntentBuilder(CustomFlutter::class.java, cachedEngineId)
        }
    }

}