import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:hive_flutter/hive_flutter.dart';

import '../services/login.dart';
import '../screens/home_screen.dart';

class LoginScreen extends StatelessWidget {
  const LoginScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
      ),
      body: ListView(
        children: [
          Center(
            child: Text(
              'IMAZINE',
              style: TextStyle(
                fontFamily: 'strasua',
                fontSize: 50,
                color: Colors.amber,
                fontWeight: FontWeight.w600,
              ),
            ),
          ),
          LoginForm(),
        ],
      ),
    );
  }
}

class LoginForm extends StatefulWidget {
  @override
  LoginFormState createState() {
    return LoginFormState();
  }
}

class LoginFormState extends State<LoginForm> {
  String? _npm;
  String? _password;

  Container formBuilder(String label, Function(String) onChanged,
          {bool obscureText = false, String Function(String?)? validator}) =>
      Container(
        padding: const EdgeInsets.only(bottom: 10),
        child: TextFormField(
          obscureText: obscureText,
          style: TextStyle(fontFamily: 'OpenSans'),
          decoration: InputDecoration(
            border: UnderlineInputBorder(),
            labelText: label,
            labelStyle: TextStyle(
              fontFamily: 'OpenSans',
            ),
          ),
          onChanged: onChanged,
          validator: validator,
        ),
      );

  void login() async {
    await loginRequest(_npm, _password).then((code) {
      if (code == 200) {
        Hive.box('prefs').put('isLoggedIn', true);
        Get.off(() => HomeScreen());
      } else {
        Fluttertoast.showToast(
          msg: "NPM/Password Salah",
          textColor: Colors.white,
          fontSize: 16.0,
        );
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    Widget fieldsSection = Container(
      padding: const EdgeInsets.all(32.0),
      child: Column(
        children: [
          formBuilder("NPM", (text) => setState(() => _npm = text)),
          formBuilder("Password", (text) => setState(() => _password = text),
              obscureText: true),
        ],
      ),
    );

    Widget loginButton = Container(
      padding: const EdgeInsets.only(left: 32.0, right: 32.0),
      child: SizedBox(
        width: double.infinity,
        child: TextButton(
          style: ButtonStyle(
            overlayColor: MaterialStateProperty.resolveWith(
                (states) => Colors.white.withOpacity(0.1)),
            backgroundColor:
                MaterialStateProperty.resolveWith((states) => Colors.amber),
          ),
          child: Text(
            'Login',
            style: TextStyle(
              color: Colors.black,
              fontWeight: FontWeight.w600,
              fontFamily: 'OpenSans',
            ),
          ),
          onPressed: login,
        ),
      ),
    );

    return Column(
      children: [
        fieldsSection,
        loginButton,
      ],
    );
  }
}
