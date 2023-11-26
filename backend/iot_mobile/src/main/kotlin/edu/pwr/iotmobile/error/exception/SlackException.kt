package edu.pwr.iotmobile.error.exception

class SlackException(body: String): Exception("Cannot send slack message, $body") {
}