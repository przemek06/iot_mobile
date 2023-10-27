package edu.pwr.iotmobile.androidimcs.model.repository.impl

import android.util.Log
import edu.pwr.iotmobile.androidimcs.data.dto.TopicDto
import edu.pwr.iotmobile.androidimcs.data.result.CreateTopicResult
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.TopicRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.repository.TopicRepository

private const val TAG = "TopicRepo"

class TopicRepositoryImpl(
    private val remoteDataSource: TopicRemoteDataSource
) : TopicRepository {
    override suspend fun createTopic(topicDto: TopicDto): CreateTopicResult {
        val result = remoteDataSource.createTopic(topicDto)
        val code = result.code()
        Log.d(TAG, "create topic result code: $code")
        return when (code) {
            200 -> CreateTopicResult.Success
            401 -> CreateTopicResult.NotAuthorized
            409 -> CreateTopicResult.AlreadyExists
            else -> CreateTopicResult.Failure
        }
    }

    override suspend fun deleteTopic(id: Int): Result<Unit> {
        val result = remoteDataSource.deleteTopic(id)
        return if (result.isSuccessful)
            Result.success(Unit)
        else
            Result.failure(Exception("Delete topic failed."))
    }

    override suspend fun getTopicsByProjectId(projectId: Int): List<TopicDto> {
        val result = remoteDataSource.getTopicsByProjectId(projectId)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body
        else
            emptyList()
    }
}