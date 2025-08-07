# screen_on_flutter

A Flutter plugin that allows you to show a custom Flutter widget when the device screen turns on.  
Useful for creating lockscreen-style popups like CashWalk or simple greeting screens.


---
### Platform Support

| Platform | Support     |
|----------|-------------|
| Android  | ✅ Supported |
| iOS      | ❌ Not supported |
| Web      | ❌ Not supported |
---

## 📌 Key Concepts & Warnings

This plugin runs on **a single shared FlutterEngine**, which makes it friendly to most state management libraries.  
It's designed to avoid the problems that arise when managing multiple FlutterEngines.

Therefore, the app is separated into two `Activity`s:
- `MainActivity`: the main app content
- `CustomFlutterActivity`: used to show the popup when the screen turns on

> ⚠️ Only one Activity can be attached to a single FlutterEngine at a time.  
> For engine stability, when `CustomFlutterActivity` is launched, the previous `MainActivity` will be detached and finished.

---


## ⚙️ Setup Instructions (Required)

### Installation

Add this to your `pubspec.yaml`:

```yaml
dependencies:
  screen_on_flutter: ^0.1.0
```
### Usage

Add the following import to your Dart code:
```dart
import 'package:screen_on_flutter/screen_on_flutter.dart';
```


### 1. AndroidManifest.xml

Edit `/android/app/src/main/AndroidManifest.xml`  
Update the `<application>` tag to use `MyApplication`:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
  <application
      android:name=".MyApplication"
      android:label="screen_on_flutter_example"
      android:icon="@mipmap/ic_launcher">
```

---

### 2. MyApplication.kt

Create a new file `MyApplication.kt` in the same folder as `MainActivity.kt`:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val engine = FlutterEngine(this)
        engine.dartExecutor.executeDartEntrypoint(
            DartEntrypoint.createDefault()
        )
        FlutterEngineCache.getInstance().put("screen_on_flutter", engine)
    }
}
```

> **Do not change the engine ID `"screen_on_flutter"`**

---

### 3. MainActivity.kt

Update `MainActivity` to use the cached Flutter engine:

```kotlin
class MainActivity : FlutterActivity() {
    override fun provideFlutterEngine(context: Context): FlutterEngine? {
        return FlutterEngineCache.getInstance().get("screen_on_flutter")
    }
}
```



---

## 🔧 Initialization Code

```dart
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
```

### `routeName` values:
- `"SCREEN_ON"` – launched from screen-on broadcast
- `"LAUNCH_SOURCE"` – launched from app icon

> Use this to route users to any widget when the screen turns on.
```md
This is where you define how the screen should respond when activated by a system-level screen-on event.
```
---

## 📲 API Usage

### ▶️ Start the service
```dart
await screenService.startService();
```

### ⏹ Stop the service
```dart
await screenService.endService();
```

### 🔙 Move to background (dismiss popup)
```dart
screenService.moveToBack();
```

### 🔐 Request permission
```dart
await screenService.requestPermission();
```

If permissions are already granted, this will be ignored.

---

## 🧪 Full Example

```dart
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
```
---

## License

This project is licensed under the MIT License.  
