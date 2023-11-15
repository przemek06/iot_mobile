package edu.pwr.iotmobile.rabbit.queue

import edu.pwr.iotmobile.rabbit.RabbitListener
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/queues")
class QueueController(
    val queueService: QueueService,
    val rabbitListener: RabbitListener

) {

    /**
     * add new queue with binding to exchange of the same name
     */
    @PostMapping
    fun registerQueue(@RequestBody queueName: String): ResponseEntity<String> {
        queueService.addQueue(queueName)
        return ResponseEntity.ok(queueName)
    }

    /**
     * Delete queue
     * force
     * - true - removes queue unconditionally
     * - false - removes queue only if empty and with no subscriber
     */
    @DeleteMapping("/{queueName}")
    fun deleteQueue(@PathVariable queueName: String, @RequestParam force:Boolean = false){
        queueService.deleteQueue(queueName, force)
    }

    /**
     * Delete all messages from queue
     */
    @PutMapping("/{queueName}")
    fun purgeQueue(@PathVariable queueName: String){
        queueService.clearQueue(queueName)
    }

    /**
     * Send string message to queue with exchange of the same name
     */
    @PostMapping("/{queueName}")
    fun sendMessage(@RequestBody message: String, @PathVariable queueName: String):ResponseEntity<String>{
        queueService.sendMessage(queueName, message)
        return ResponseEntity.ok(message)
    }

    /**
     * Add new listener
     */
    @GetMapping("/subscribe/{queueName}")
    fun subscribeQueue(@PathVariable queueNames: List<String>):ResponseEntity<String> {
            rabbitListener.registerConsumer(queueNames)
        return ResponseEntity.ok("Added new listener to queue $queueNames")
    }

    /**
     * Delete queue listener
     */
    @DeleteMapping("/subscribe/{queueName}")
    fun unsubscribeQueue(@PathVariable queueName: String):ResponseEntity<String> {
        rabbitListener.cancelConsumer(queueName)
        return ResponseEntity.ok("Removed listener from queue $queueName")
    }

}