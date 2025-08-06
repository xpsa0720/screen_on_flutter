package com.example.screen_on_flutter_example

import android.app.Application
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor.DartEntrypoint

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val engine = FlutterEngine(this)
        engine.dartExecutor.executeDartEntrypoint(
            DartEntrypoint.createDefault()
        )
        FlutterEngineCache.getInstance().put("screen_on_flutter", engine)
    }
}