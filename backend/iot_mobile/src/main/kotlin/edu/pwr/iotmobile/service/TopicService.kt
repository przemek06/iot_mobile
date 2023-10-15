package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.entities.Topic
import edu.pwr.iotmobile.rabbit.queue.QueueService
import edu.pwr.iotmobile.repositories.TopicRepository
import org.springframework.stereotype.Service

@Service
class TopicService(
    val topicRepository: TopicRepository,
    val queueService: QueueService
) {

    fun registerQueue(topic: Topic){
        queueService.addQueue(topic.name)
    }

    fun saveTopic(topic: Topic): Topic{
        topicRepository.save(topic)
        return topic
    }

    fun addTopic(topic: Topic):Topic{
        registerQueue(topic)
        return saveTopic(topic)
    }

    fun getAllTopics(): List<Topic>{
        return topicRepository.findAll()
    }

}