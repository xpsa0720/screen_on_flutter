import 'package:flutter/material.dart';

/// The [AlarmModel] is used to display an alarm before the service starts.
class AlarmModel {
  /// The [title] is the title of the alarm.
  final String title;

  /// The [content] is the body text of the alarm.

  final String content;

  /// The [small_icon_path] is the file path for displaying a large icon.
  /// This field is optional.
  AlarmModel({
    required this.content,
    required this.title,
  });

  Map<String, dynamic> toJson() => {
        "title": title,
        "content": content,
      };
}
