package com.example.screen_on_flutter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat.startActivityForResult
import io.flutter.FlutterInjector
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor.DartEntrypoint
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

/** ScreenOnFlutterPlugin */
class ScreenOnFlutterPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var context: Context
    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "screen_on_flutter")
        channel.setMethodCallHandler(this)

        context = flutterPluginBinding.applicationContext
    }


    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getPlatformVersion" -> handleGetPlatformVersion(result)
            "requestPermission" -> getPermission(result)
            "startService" -> startScreenService(result, call.argument<String>("entryPointName"))
            "endService" -> endScreenService(result)
            else -> result.notImplemented()
        }
    }

    private fun endScreenService(result: MethodChannel.Result) {
        try {
            val service = Intent(context, ScreenService::class.java)
            context.stopService(service)
            result.success("success")
        } catch (e: Exception) {
            result.success(e.toString())
        }

    }

    private fun startScreenService(result: MethodChannel.Result, entryPointName: String?) {
        try {
            println("엔트리 포인트 이름: ${entryPointName.toString()}")
            createFlutterEngine(entryPointName)
            val service = Intent(context, ScreenService::class.java)
            service.putExtra("entryPointName",entryPointName)
            context.startService(service)
            result.success("success")
        } catch (e: Exception) {
            result.success(e.toString())
        }

    }

    private fun getPermission(result: MethodChannel.Result) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(context)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.packageName)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
            result.success("success")
        } catch (e: Exception) {
            result.success(e.toString())
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun handleGetPlatformVersion(result: MethodChannel.Result) {
        result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }

    fun createFlutterEngine(name:String?): FlutterEngine {
        val LockScreen_flutterEngine = FlutterEngine(context)
        val flutterLoader = FlutterInjector.instance().flutterLoader()
        flutterLoader.startInitialization(context)
        flutterLoader.ensureInitializationComplete(context, null)
        if (!flutterLoader.initialized()) {
            throw AssertionError(
                "DartEntrypoints can only be created once a FlutterEngine is created."
            );
        }
        LockScreen_flutterEngine.dartExecutor.executeDartEntrypoint(
            DartEntrypoint(
                flutterLoader.findAppBundlePath(),
                name?:"main2"
            )
        )
        FlutterEngineCache.getInstance().put("LockScreenEngine_id", LockScreen_flutterEngine)
        return LockScreen_flutterEngine
    }
}
