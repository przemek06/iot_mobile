package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.TopicDto

interface TopicRepository {
    suspend fun createTopic(topicDto: TopicDto)
    suspend fun deleteTopic(id: Int): Result<Unit>
    suspend fun getTopicsByProjectId(projectId: Int): List<TopicDto>
}