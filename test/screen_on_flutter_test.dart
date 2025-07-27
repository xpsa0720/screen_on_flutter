import 'package:flutter_test/flutter_test.dart';
import 'package:screen_on_flutter/screen_on_flutter.dart';
import 'package:screen_on_flutter/screen_on_flutter_platform_interface.dart';
import 'package:screen_on_flutter/screen_on_flutter_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockScreenOnFlutterPlatform
    with MockPlatformInterfaceMixin
    implements ScreenOnFlutterPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final ScreenOnFlutterPlatform initialPlatform = ScreenOnFlutterPlatform.instance;

  test('$MethodChannelScreenOnFlutter is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelScreenOnFlutter>());
  });

  test('getPlatformVersion', () async {
    ScreenOnFlutter screenOnFlutterPlugin = ScreenOnFlutter();
    MockScreenOnFlutterPlatform fakePlatform = MockScreenOnFlutterPlatform();
    ScreenOnFlutterPlatform.instance = fakePlatform;

    expect(await screenOnFlutterPlugin.getPlatformVersion(), '42');
  });
}
