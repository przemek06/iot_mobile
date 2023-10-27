package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.TopicDto
import edu.pwr.iotmobile.androidimcs.data.result.CreateTopicResult

interface TopicRepository {
    suspend fun createTopic(topicDto: TopicDto): CreateTopicResult
    suspend fun deleteTopic(id: Int): Result<Unit>
    suspend fun getTopicsByProjectId(projectId: Int): List<TopicDto>
}