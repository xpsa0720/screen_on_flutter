import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'class/status_class.dart';
import 'screen_on_flutter_platform_interface.dart';

class MethodChannelScreenOnFlutter extends ScreenOnFlutterPlatform {
  @visibleForTesting
  final methodChannel = const MethodChannel('screen_on_flutter');

  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>(
      'getPlatformVersion',
    );
    return version;
  }

  Future<String> startService_methodChannel(String entryPointName) async {
    final result = await methodChannel.invokeMethod<String>(
      "startService",
      <String, dynamic>{"entryPointName": entryPointName},
    );
    if (result == null) return "return null wrong!!";
    return result.toString();
  }

  Future<String> endService_methodChannel(String entryPointName) async {
    final result = await methodChannel.invokeMethod<String>(
      "endService",
      <String, dynamic>{},
    );
    if (result == null) return "return null wrong!!";
    return result.toString();
  }

  Future<String> requestPermission_methodChannel(String entryPointName) async {
    final result = await methodChannel.invokeMethod<String>(
      "requestPermission",
      <String, dynamic>{},
    );
    if (result == null) return "return null wrong!!";
    return result.toString();
  }
}
