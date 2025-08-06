package com.example.screen_on_flutter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

/** ScreenOnFlutterPlugin */
class ScreenOnFlutterPlugin : FlutterPlugin, MethodCallHandler, ActivityAware{
    private lateinit var context: Context
    private lateinit var channel: MethodChannel
    private var flutter_activity: Activity? = null
    var eventChannelHandler: EventChannelHandler? = null



    companion object{
        var main_activity: Activity? = null
        var last_attach: String? = null

        fun mainActivityFinishAffinity(){
            val engine = FlutterEngineCache.getInstance().get("screen_on_flutter")
            if(main_activity != null && engine != null && last_attach == "CustomFlutter") {
                main_activity!!.finishAndRemoveTask()
                main_activity = null
            }
        }
    }

    object EventChannelBridge {
        var eventChannelHandler: EventChannelHandler? = null
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "screen_on_flutter_MethodChannel")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext

        eventChannelHandler = EventChannelHandler(flutterPluginBinding.binaryMessenger,"screen_on_flutter_EventChannel")
        EventChannelBridge.eventChannelHandler = eventChannelHandler;
    }


    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getPlatformVersion" -> handleGetPlatformVersion(result)
            "requestPermission" -> getPermission(result)
            "startService" -> startScreenService(result)
            "moveToBack" -> moveToBack(result)
            "endService" -> endScreenService(result)
            else -> result.notImplemented()
        }
    }

    private fun moveToBack(result: MethodChannel.Result){
        if(flutter_activity!= null){
            flutter_activity!!.finishAffinity();
            flutter_activity = null
        }
        mainActivityFinishAffinity()
        result.success("success")
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

    private fun startScreenService(result: MethodChannel.Result) {
        try {
            val service = Intent(context, ScreenService::class.java)
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

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        val activity = binding.activity

        when (activity.javaClass.simpleName) {
            "MainActivity" -> {
                main_activity = activity
                last_attach = "MainActivity"
            }
            "CustomFlutter" -> {
                flutter_activity = activity
                last_attach = "CustomFlutter"
            }
        }

        if(main_activity != null  && main_activity!!.isDestroyed){
            main_activity = null;
        }
        if(flutter_activity != null  && flutter_activity!!.isDestroyed){
            flutter_activity = null;
        }


        val intent = activity.intent
        val isFromLancher = intent?.action == Intent.ACTION_MAIN && intent.categories?.contains(Intent.CATEGORY_LAUNCHER) == true

        if(isFromLancher){
            if(EventChannelBridge.eventChannelHandler != null){
                EventChannelBridge.eventChannelHandler?.sink("LAUNCH_SOURCE");
            }
        }
    }


    override fun onDetachedFromActivityForConfigChanges() {
        main_activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        main_activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        if(main_activity != null  && main_activity!!.isDestroyed){
            main_activity = null;
        }
        if(flutter_activity != null  && flutter_activity!!.isDestroyed){
            flutter_activity = null;
        }
        mainActivityFinishAffinity()
    }


}
