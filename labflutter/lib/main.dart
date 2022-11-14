import 'package:flutter/cupertino.dart';
import 'package:labflutter/mainpage.dart';
import 'package:labflutter/storage.dart';
import 'package:provider/provider.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return CupertinoApp(
      home: MultiProvider(
        providers: [
          ChangeNotifierProvider.value(
            value: Storage(),
          ),
        ],
        child: Center (
            child: MainPage(),
        )
        ,
      ),
    );
  }
}
