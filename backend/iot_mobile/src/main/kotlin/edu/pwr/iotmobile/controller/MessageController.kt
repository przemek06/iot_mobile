package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.dto.TopicMessagesDTO
import edu.pwr.iotmobile.service.MessageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class MessageController(
    val messageService: MessageService
) {

    /**
     * Send message to specified queue
     */
    @PostMapping("/user/messages")
    fun sendMessage(@RequestBody message: MessageDTO): ResponseEntity<MessageDTO> {
        return ResponseEntity.ok(messageService.sendMessage(message))
    }

    @PostMapping("/anon/messages/device")
    fun sendMessageFromDevice(@RequestBody message: MessageDTO): ResponseEntity<MessageDTO> {
        return ResponseEntity.ok(messageService.sendMessageFromDevice(message))
    }

    @GetMapping("/user/messages/{dashboardId}/{n}")
    fun getLastMessagesForDashboard(
        @PathVariable dashboardId: Int,
        @PathVariable n: Int
    ): ResponseEntity<List<TopicMessagesDTO>> {
        return ResponseEntity.ok(messageService.getLastMessagesForDashboard(dashboardId, n))
    }

}