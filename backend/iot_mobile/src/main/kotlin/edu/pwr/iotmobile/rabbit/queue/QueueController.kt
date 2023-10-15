package edu.pwr.iotmobile.rabbit.queue

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/queues")
class QueueController(
    val queueService: QueueService,

) {


//    @PostMapping
//    fun registerQueue(@RequestBody queueName: String): ResponseEntity<String> {
//        queueService.addQueue(queueName)
//        return ResponseEntity.ok(queueName)
//    }

    @PostMapping("/{queueName}")
    fun sendMessage(@RequestBody message: String, @PathVariable queueName: String):ResponseEntity<String>{
        queueService.sendMessage(queueName, message)
        return ResponseEntity.ok(message)
    }
}