import 'package:http/http.dart' as http;

import '../utils/config.dart';
import '../utils/logger.dart';

Future loginRequest(
  String? username,
  String? password,
) async {
  var request = http.MultipartRequest('POST', Uri.parse('$loginUrl'));
  request.fields.addAll({
    'username': username!,
    'password': password!,
  });

  http.StreamedResponse response = await request.send();

  logger.v(response);
  return response.statusCode;
}
