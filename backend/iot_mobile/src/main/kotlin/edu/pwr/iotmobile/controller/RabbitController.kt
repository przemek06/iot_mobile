package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.rabbit.RabbitChannel
import edu.pwr.iotmobile.rabbit.RabbitQueue
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rabbit")
class RabbitController(
    val rabbitChannel: RabbitChannel,
    val queue: RabbitQueue
) {

    @GetMapping("/start")
    fun openChannel(){
        val channel = rabbitChannel.createChannel()

        if (channel != null) {
            rabbitChannel.closeChannel(channel)
            queue.addQueue(channel,"helo", "helo")
        }
    }

    @PostMapping("/message/{queueName}")
    fun sendMessage(@PathVariable queueName: String,@RequestBody message: MessageDTO): ResponseEntity<String> {
        val channel = rabbitChannel.createChannel()
        if (channel != null) {
            queue.publishMessage(channel, queueName, queueName, message.toEntity() )

//            rabbitChannel.closeChannel(channel)
        }
        return ResponseEntity.ok(message.message)
    }

}