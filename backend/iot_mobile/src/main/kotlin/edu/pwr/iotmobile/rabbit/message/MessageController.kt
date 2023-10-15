package edu.pwr.iotmobile.rabbit.message

import edu.pwr.iotmobile.entities.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/messages")
class MessageController(
    val messageService: MessageService
) {

    @PostMapping
    fun sendMessage(@RequestBody message: Message):ResponseEntity<String>{
        messageService.sendMessage(message)
        return ResponseEntity.ok(message.message)
    }

}