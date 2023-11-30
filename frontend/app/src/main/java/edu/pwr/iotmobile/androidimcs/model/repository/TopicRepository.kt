package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.TopicDto
import edu.pwr.iotmobile.androidimcs.data.result.CreateResult

interface TopicRepository {
    suspend fun createTopic(topicDto: TopicDto): CreateResult
    suspend fun deleteTopic(id: Int): Result<Unit>
    suspend fun getTopicsByProjectId(projectId: Int): List<TopicDto>
}