import 'package:flutter/material.dart';

final String baseUrl = "http://himatif.fmipa.unpad.ac.id/wp-json/wp/v2";
final String loginUrl = "https://api.himatif.org/data/v1/user/login";

class MyScrollBehaviour extends ScrollBehavior {
  @override
  Widget buildViewportChrome(
      BuildContext context, Widget child, AxisDirection axisDirection) {
    return child;
    // return GlowingOverscrollIndicator(
    //   axisDirection: AxisDirection.down,
    //   showLeading: true,
    //   showTrailing: true,
    //   color: Colors.grey[300],
    //   child: child,
    // );
  }
}
