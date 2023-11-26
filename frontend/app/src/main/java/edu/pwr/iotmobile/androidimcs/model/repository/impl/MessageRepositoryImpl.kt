package edu.pwr.iotmobile.androidimcs.model.repository.impl

import edu.pwr.iotmobile.androidimcs.data.dto.MessageDto
import edu.pwr.iotmobile.androidimcs.data.dto.TopicMessagesDto
import edu.pwr.iotmobile.androidimcs.model.datasource.remote.MessageRemoteDataSource
import edu.pwr.iotmobile.androidimcs.model.repository.MessageRepository

class MessageRepositoryImpl(
    private val remoteDataSource: MessageRemoteDataSource
) : MessageRepository {
    override suspend fun sendMessage(message: MessageDto): Result<Unit> {
        val result = remoteDataSource.sendMessage(message)
        return if (result.isSuccessful)
            Result.success(Unit)
        else
            Result.failure(Exception("Send message failed"))
    }

    override suspend fun getLastMessagesForDashboard(
        dashboardId: Int,
        numOfMessages: Int
    ): List<TopicMessagesDto> {
        val result = remoteDataSource.getLastMessagesForDashboard(dashboardId, numOfMessages)
        val body = result.body()
        return if (result.isSuccessful && body != null)
            body
        else
            emptyList()
    }
}