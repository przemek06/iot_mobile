package edu.pwr.iotmobile.error.exception

class TelegramException(body: String) : Exception("Cannot send telegram message, $body") {
}