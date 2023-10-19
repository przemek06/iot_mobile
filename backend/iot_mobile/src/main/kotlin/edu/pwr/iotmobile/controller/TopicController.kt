package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.TopicDTO
import edu.pwr.iotmobile.service.TopicService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class TopicController(val topicService: TopicService) {
    @PostMapping("/user/topic")
    fun createTopic(@Valid @RequestBody topic: TopicDTO): ResponseEntity<TopicDTO> {
        return ResponseEntity.ok(topicService.createTopic(topic))
    }

    @DeleteMapping("/user/topic/{topicId}")
    fun deleteTopic(@PathVariable topicId: Int): ResponseEntity<Unit> {
        return if (topicService.deleteTopic(topicId)) ResponseEntity.ok().build()
        else ResponseEntity.noContent().build()
    }

    @GetMapping("/user/dashboard/{projectId}")
    fun findAllTopicsInProject(@PathVariable projectId: Int) : ResponseEntity<List<TopicDTO>> {
        return ResponseEntity.ok(topicService.findAllTopicsInProject(projectId))
    }
}