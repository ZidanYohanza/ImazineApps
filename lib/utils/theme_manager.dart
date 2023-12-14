import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:hive_flutter/hive_flutter.dart';

enum GlobalTheme { dark, light }

GlobalTheme globalTheme = GlobalTheme.light;

void setTheme(GlobalTheme theme) async {
  applyTheme(theme);
  await Hive.box('prefs')
      .put('theme', theme == GlobalTheme.dark ? 'dark' : 'light');
}

void getTheme() async {
  GlobalTheme theme =
      (Hive.box('prefs').get('theme', defaultValue: 'light') == 'light'
          ? GlobalTheme.dark
          : GlobalTheme.light);
  applyTheme(theme);
}

void applyTheme(GlobalTheme theme) {
  Get.changeThemeMode(
      theme == GlobalTheme.dark ? ThemeMode.dark : ThemeMode.light);
  globalTheme = theme;
}
