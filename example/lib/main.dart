import 'package:flutter/material.dart';
import 'package:screen_on_flutter/screen_on_flutter.dart';

final GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  late final ScreenOnFlutter screenService;

  screenService = ScreenOnFlutter(
    routeCallback: (routeName) {
      debugPrint('App launched from: $routeName');

      if (routeName == "SCREEN_ON") {
        Future.microtask(() {
          navigatorKey.currentState?.pushReplacement(
            MaterialPageRoute(
              builder: (_) => LockScreen(service: screenService),
            ),
          );
        });
      }
    },
  );

  await screenService.startService();
  await screenService.requestPermission();
  runApp(MyApp(service: screenService));
}

class MyApp extends StatelessWidget {
  final ScreenOnFlutter service;

  const MyApp({super.key, required this.service});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      navigatorKey: navigatorKey,
      home: HomeScreen(service: service),
    );
  }
}

class HomeScreen extends StatelessWidget {
  final ScreenOnFlutter service;

  const HomeScreen({super.key, required this.service});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Screen On Flutter Example')),
      body: Center(
        child: ElevatedButton(
          onPressed: () {
            service.endService();
          },
          child: const Text('Stop Service'),
        ),
      ),
    );
  }
}

class LockScreen extends StatelessWidget {
  final ScreenOnFlutter service;

  const LockScreen({super.key, required this.service});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.sunny, color: Colors.orange, size: 80),
            const SizedBox(height: 20),
            const Text(
              'This Screen Flutter Widget',
              style: TextStyle(color: Colors.black, fontSize: 24),
            ),
            const SizedBox(height: 40),
            ElevatedButton(
              onPressed: () {
                service.moveToBack();
              },
              child: const Text('Close'),
            ),
          ],
        ),
      ),
    );
  }
}
