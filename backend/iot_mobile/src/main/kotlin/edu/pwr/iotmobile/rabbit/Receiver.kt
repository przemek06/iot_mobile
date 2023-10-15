package edu.pwr.iotmobile.rabbit

import org.springframework.stereotype.Component

@Component
class Receiver {

    fun receiveMessage(message: String) {
        println("Received <$message>")
    }
}