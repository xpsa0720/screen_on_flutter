import 'dart:ui';

import 'package:flutter/services.dart';
import 'package:screen_on_flutter/class/status_class.dart';

import 'screen_on_flutter_platform_interface.dart';
import 'screen_on_flutter_method_channel.dart';

class ScreenOnFlutter {
  ChannelScreenOnFlutter channel;
  final void Function(dynamic) routeCallback;
  ScreenOnFlutter({
    ChannelScreenOnFlutter? MethodChannel,
    required this.routeCallback,
  }) : channel = MethodChannel ?? ChannelScreenOnFlutter();

  Future<String?> getPlatformVersion() {
    return ScreenOnFlutterPlatform.instance.getPlatformVersion();
  }

  Future<StatusBase> requestPermission() async {
    final result = await channel.requestPermission_methodChannel();
    return StatusBase.fromMap(result.toString());
  }

  Future<StatusBase> startService() async {
    final result = await channel.startService_methodChannel();
    channel.startListenScreenOn(routeCallback: routeCallback);
    return StatusBase.fromMap(result.toString());
  }

  Future<StatusBase> endService() async {
    final result = await channel.endService_methodChannel();
    channel.cancelListenScreenOn();
    return StatusBase.fromMap(result.toString());
  }

  Future<StatusBase> moveToBack() async {
    final result = await channel.moveToBack_methodChannel();
    return StatusBase.fromMap(result.toString());
  }
}
