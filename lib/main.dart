import 'package:flutter/material.dart';

import 'package:flutter/services.dart';
import 'package:get/route_manager.dart';
import 'package:intl/date_symbol_data_local.dart';
import 'package:hive_flutter/hive_flutter.dart';

import '../screens/login.dart';
import '../screens/home_screen.dart';
import '../utils/config.dart';
import '../utils/theme_manager.dart';

Future<void> main() async {
  await initializeDateFormatting("id_ID", null);
  await Hive.initFlutter();
  await Hive.openBox('prefs').then(
    (box) {
      setTheme(box.get('theme', defaultValue: 'light') == 'light'
          ? GlobalTheme.light
          : GlobalTheme.dark);
      runApp(
        GetMaterialApp(
          title: 'Imazine App',
          debugShowCheckedModeBanner: false,
          builder: (context, child) => ScrollConfiguration(
            behavior: MyScrollBehaviour(),
            child: child!,
          ),
          theme: ThemeData.light().copyWith(
            primaryColor: Colors.amber,
            colorScheme: ColorScheme.fromSwatch().copyWith(secondary: Colors.grey),
          ),
          darkTheme: ThemeData.dark().copyWith(
            primaryColor: Colors.amber,
            colorScheme: ColorScheme.fromSwatch().copyWith(secondary: Colors.grey),
          ),
          themeMode: box.get('theme', defaultValue: 'light') == 'light'
              ? ThemeMode.light
              : ThemeMode.dark,
          // theme: ThemeData(
          //   cardColor: Colors.white,
          //   appBarTheme: AppBarTheme(
          //     color: Colors.white,
          //   ),
          //   canvasColor: Colors.white,
          //   primaryColor: Colors.amber,
          //   accentColor: Colors.grey[100],
          // ),
          home: MyApp(),
        ),
      );
    },
  );
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    SystemChrome.setPreferredOrientations([
      DeviceOrientation.portraitUp,
      DeviceOrientation.portraitDown,
    ]);

    // SystemChrome.setSystemUIOverlayStyle(
    //   SystemUiOverlayStyle(
    //       systemNavigationBarColor: Colors.grey[50], // navigation bar color
    //       systemNavigationBarDividerColor: Colors.black26,
    //       systemNavigationBarIconBrightness: Brightness.dark,
    //       statusBarIconBrightness: Brightness.dark,
    //       statusBarColor: Colors.transparent // status bar color
    //       ),
    // );

    return Hive.box('prefs').get('isLoggedIn', defaultValue: false)
        ? HomeScreen()
        : LoginScreen();
  }
}
