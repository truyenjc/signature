
import 'dart:async';

import 'package:flutter/services.dart';

class Signature {
  static const MethodChannel _channel =
      const MethodChannel('com.truyenjc.signature');

  static Future<String> get sha1 async {
    final String sha1 = await _channel.invokeMethod('getSha1');
    return sha1;
  }
}
