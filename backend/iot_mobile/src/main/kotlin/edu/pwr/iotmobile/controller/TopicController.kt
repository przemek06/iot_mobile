package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.TopicConnectionDTO
import edu.pwr.iotmobile.dto.TopicDTO
import edu.pwr.iotmobile.service.TopicService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class TopicController(val topicService: TopicService) {
    // 400, 401, 403, 409
    @PostMapping("/user/topic")
    fun createTopic(@Valid @RequestBody topic: TopicDTO): ResponseEntity<TopicDTO> {
        return ResponseEntity.ok(topicService.createTopic(topic))
    }

    // 400, 401, 403, 409 when topic is used
    @DeleteMapping("/user/topic/{topicId}")
    fun deleteTopic(@PathVariable topicId: Int): ResponseEntity<Unit> {
        return if (topicService.deleteTopic(topicId)) ResponseEntity.ok().build()
        else ResponseEntity.noContent().build()
    }

    // 400, 401, 403
    @GetMapping("/user/topic/{projectId}")
    fun findAllTopicsInProject(@PathVariable projectId: Int): ResponseEntity<List<TopicDTO>> {
        return ResponseEntity.ok(topicService.findAllTopicsInProject(projectId))
    }

    // 400, 401, 403
    @GetMapping("/user/topic/used/{topicId}")
    fun isTopicUsed(@PathVariable topicId: Int): ResponseEntity<Boolean> {
        return ResponseEntity.ok(topicService.isTopicUsed(topicId))
    }

    @GetMapping("/anon/topic/device")
    fun findForDevice(@RequestBody topicConnectionDTO: TopicConnectionDTO): ResponseEntity<TopicDTO> {
        return ResponseEntity.ok(topicService.findForDevice(topicConnectionDTO))
    }
}