package edu.pwr.iotmobile.rabbit.message

import edu.pwr.iotmobile.dto.MessageDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/messages")
class MessageController(
    val messageService: MessageService
) {

    /**
     * Send message to specified queue
     */
    @PostMapping
    fun sendMessage(@RequestBody message: MessageDTO):ResponseEntity<MessageDTO>{
        messageService.sendMessage(message.toEntity())
        return ResponseEntity.ok(message)
    }

}