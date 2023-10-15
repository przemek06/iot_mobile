package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.TopicDTO
import edu.pwr.iotmobile.entities.Topic
import edu.pwr.iotmobile.repositories.TopicRepository
import edu.pwr.iotmobile.service.TopicService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/topics")
class TopicController(

    val topicService: TopicService
) {

    @PostMapping
    fun registerTopic(@RequestBody topicDTO: TopicDTO): ResponseEntity<TopicDTO>{
        val topic: Topic = topicService.addTopic(topicDTO.toEntity())
        return ResponseEntity.ok(topic.toDTO())
    }

    @GetMapping
    fun getAllTopics(): ResponseEntity<List<TopicDTO>>{
        val topics: List<Topic> = topicService.getAllTopics()
        return ResponseEntity.ok(topics.map{it.toDTO()})
    }

}