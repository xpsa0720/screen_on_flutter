import 'package:screen_on_flutter/class/alarm_model.dart';
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

  /// Requests the necessary permissions.
  ///
  /// This method requests both the notification permission
  /// and the "draw over other apps" permission.
  Future<StatusBase> requestPermission() async {
    final result = await channel.requestPermission_methodChannel();
    return StatusBase.fromMap(result.toString());
  }

  /// Starts the service.
  ///
  /// You must call [requestPermission] before invoking this method.
  Future<StatusBase> startService({
    required AlarmModel model,
  }) async {
    final result = await channel.startService_methodChannel(
        model: AlarmModel(
      content: model.content,
      title: model.title,
    ));
    channel.startListenScreenOn(routeCallback: routeCallback);
    return StatusBase.fromMap(result.toString());
  }

  /// Stops the service.
  Future<StatusBase> endService() async {
    final result = await channel.endService_methodChannel();
    channel.cancelListenScreenOn();
    return StatusBase.fromMap(result.toString());
  }

  /// The [moveToBack] method closes the screen that appears when the device is turned on.
  Future<StatusBase> moveToBack() async {
    final result = await channel.moveToBack_methodChannel();
    return StatusBase.fromMap(result.toString());
  }
}
