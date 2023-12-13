package edu.pwr.iotmobile.androidimcs.model.repository

import edu.pwr.iotmobile.androidimcs.data.dto.MessageDto
import edu.pwr.iotmobile.androidimcs.data.dto.TopicMessagesDto

interface MessageRepository {
    suspend fun sendMessage(message: MessageDto): Result<Unit>
    suspend fun getLastMessagesForDashboard(
        dashboardId: Int,
        numOfMessages: Int = 20
    ): List<TopicMessagesDto>
}