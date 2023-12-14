package com.himatifunpad.imazine.util

import java.io.IOException

class ApiException(message: String) : IOException(message)
class NoInternetException : IOException("You're not connected to the internet")
