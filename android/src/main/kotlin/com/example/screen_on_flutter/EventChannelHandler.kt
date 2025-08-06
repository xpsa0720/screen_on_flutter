package com.example.screen_on_flutter

import android.os.Handler
import android.os.Looper
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel

class EventChannelHandler(binaryMessenger: BinaryMessenger, name: String) : EventChannel.StreamHandler{
    var eventChannel: EventChannel? = null
    var eventSink: EventChannel.EventSink? = null

    init {
        eventChannel = EventChannel(binaryMessenger, name)
        eventChannel!!.setStreamHandler(this)
    }

    override fun onListen(
        arguments: Any?,
        events: EventChannel.EventSink?
    ) {
        if(events!=null){
            eventSink = events
            events.success(arguments);
        }
    }

    override fun onCancel(arguments: Any?) {
        if (arguments != null) {
            sink(arguments)
        }
        eventChannel?.setStreamHandler(null)
        eventChannel = null
        eventSink = null
    }

    fun sink(value: Any) {
        Handler(Looper.getMainLooper()).post {
            eventSink?.success(value)
        }
    }
}