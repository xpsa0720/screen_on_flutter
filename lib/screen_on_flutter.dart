import 'package:screen_on_flutter/class/status_class.dart';

import 'screen_on_flutter_platform_interface.dart';
import 'screen_on_flutter_method_channel.dart';

class ScreenOnFlutter {
  MethodChannelScreenOnFlutter methodChannel;
  final String entryPointName;
  ScreenOnFlutter({
    required this.entryPointName,
    MethodChannelScreenOnFlutter? MethodChannel,
  }) : methodChannel = MethodChannel ?? MethodChannelScreenOnFlutter();

  Future<String?> getPlatformVersion() {
    return ScreenOnFlutterPlatform.instance.getPlatformVersion();
  }

  Future<StatusBase> requestPermission() async {
    final result = await methodChannel.requestPermission_methodChannel(
      entryPointName,
    );
    return StatusBase.fromMap(result.toString());
  }

  Future<StatusBase> startService() async {
    final result = await methodChannel.startService_methodChannel(
      entryPointName,
    );
    return StatusBase.fromMap(result.toString());
  }

  Future<StatusBase> endService() async {
    final result = await methodChannel.endService_methodChannel(entryPointName);
    return StatusBase.fromMap(result.toString());
  }
}
