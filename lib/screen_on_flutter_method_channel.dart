import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'class/alarm_model.dart';
import 'screen_on_flutter_platform_interface.dart';

class ChannelScreenOnFlutter extends ScreenOnFlutterPlatform {
  @visibleForTesting
  final methodChannel = const MethodChannel('screen_on_flutter_MethodChannel');
  final eventChannel = const EventChannel("screen_on_flutter_EventChannel");
  StreamSubscription? _streamSubscription;
  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>(
      'getPlatformVersion',
    );
    return version;
  }

  Future<String> startService_methodChannel({
    required AlarmModel model,
  }) async {
    final result = await methodChannel.invokeMethod<String>(
        "startService", <String, dynamic>{"AlarmModel": model.toJson()});
    if (result == null) return "return null wrong!!";
    return result.toString();
  }

  Future<String> endService_methodChannel() async {
    final result = await methodChannel.invokeMethod<String>("endService");
    if (result == null) return "return null wrong";
    return result.toString();
  }

  Future<String> requestPermission_methodChannel() async {
    final result = await methodChannel.invokeMethod<String>(
      "requestPermission",
    );
    if (result == null) return "return null wrong";
    return result.toString();
  }

  Future<String> moveToBack_methodChannel() async {
    final result = await methodChannel.invokeMethod<String>("moveToBack");
    if (result == null) return "return null wrong";
    return result.toString();
  }

  void startListenScreenOn({
    required void Function(dynamic) routeCallback,
    Function? onError,
    void Function()? onDone,
    bool? cancelOnError,
  }) {
    _streamSubscription = eventChannel.receiveBroadcastStream().listen(
          routeCallback,
          onError: onError,
          onDone: onDone,
          cancelOnError: cancelOnError,
        );
  }

  void cancelListenScreenOn() {
    if (_streamSubscription != null) {
      _streamSubscription!.cancel();
      _streamSubscription = null;
    }
  }
}
