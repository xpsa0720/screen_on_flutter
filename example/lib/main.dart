import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:screen_on_flutter/screen_on_flutter.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  final a = ScreenOnFlutter(entryPointName: "main2");
  a.startService();
  runApp(MaterialApp(home: MyApp(a: a)));
}

@pragma('vm:entry-point')
void main2() {
  print("main2 실행");
  runApp(MaterialApp(home: MyApp2()));
}

class MyApp2 extends StatefulWidget {
  const MyApp2({super.key});

  @override
  State<MyApp2> createState() => _MyApp2State();
}

class _MyApp2State extends State<MyApp2> with SingleTickerProviderStateMixin {
  late AnimationController _controller;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(vsync: this);
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(body: Column(children: [Text("Aaaaaaaaaaaaaaaaaaaaaaaa")]));
  }
}

class MyApp extends StatefulWidget {
  final ScreenOnFlutter a;
  const MyApp({super.key, required this.a});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: ElevatedButton(
          onPressed: () {
            widget.a.endService();
          },
          child: Text("끝"),
        ),
      ),
    );
  }
}
