package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.ComponentListDTO
import edu.pwr.iotmobile.dto.MessageDTO
import edu.pwr.iotmobile.dto.TopicMessagesDTO
import edu.pwr.iotmobile.entities.Dashboard
import edu.pwr.iotmobile.entities.InputComponent
import edu.pwr.iotmobile.entities.Message
import edu.pwr.iotmobile.entities.Topic
import edu.pwr.iotmobile.error.exception.InvalidDataException
import edu.pwr.iotmobile.error.exception.NoAuthenticationException
import edu.pwr.iotmobile.error.exception.NotAllowedException
import edu.pwr.iotmobile.error.exception.QueueException
import edu.pwr.iotmobile.rabbit.queue.QueueService
import edu.pwr.iotmobile.repositories.MessageRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class MessageServiceTest {

    private val messageRepository = mockk<MessageRepository>()
    private val userService = mockk<UserService>()
    private val dashboardService = mockk<DashboardService>()
    private val projectService = mockk<ProjectService>()
    private val componentService = mockk<ComponentService>()
    private val topicService = mockk<TopicService>()
    private val queueService = mockk<QueueService>()
    private val messageService = MessageService(
        messageRepository = messageRepository,
        userService = userService,
        dashboardService = dashboardService,
        projectService = projectService,
        componentService = componentService,
        topicService = topicService,
        queueService = queueService
    )

    private fun getTopic(): Topic {
        val topic = Topic()
        topic.id = 1
        topic.project.id = 1
        topic.project.connectionKey = ""
        return topic
    }

    private fun getMessage(id: Int = 1): Message {
        val message = Message()
        message.id = id
        message.topic = getTopic()
        message.tsSent = ""
        return message
    }

    private fun getMessageDto(): MessageDTO {
        val messageDTO = getMessage().toDTO()
        messageDTO.connectionKey = ""
        messageDTO.tsSent = ""
        return messageDTO
    }

    private fun getListOfMessages(start: Int = 1, stop: Int): MutableList<Message> {
        val msg_list = mutableListOf(getMessage(start))
        for (i in start..stop) {
            msg_list.add(getMessage(i))
        }
        return msg_list
    }

    private fun getDashboard(): Dashboard {
        val dashboard = Dashboard()
        dashboard.project.id = 1
        return dashboard
    }

    private fun getComponents(): ComponentListDTO {
        val component = InputComponent()
        component.dashboard = getDashboard()
        component.topic = getTopic()
        return ComponentListDTO(1, listOf(component).map { it.toDTO() })
    }


    @Test
    fun sendMessagePositive() {
        // given
        val topic = getTopic()
        val message = getMessage()
        val messageDTO = getMessageDto()
        every { userService.getActiveUserId() } returns 1
        every { topicService.findTopic(any()) } returns topic
        every { projectService.isInProject(any(), any()) } returns true
        every { messageRepository.save(any()) } returns message
        every { queueService.sendMessage(any(), any()) } returns Unit

        // when
        val actual = messageService.sendMessage(messageDTO)

        // then
        assertEquals(messageDTO, actual)
    }

    @Test
    fun sendMessageUserNotAuthenticated() {
        // given
        val message = getMessage()
        every { userService.getActiveUserId() } returns null

        // throws
        assertThrows<NoAuthenticationException> { messageService.sendMessage(message.toDTO()) }
    }

    @Test
    fun sendMessageRabbitError() {
        // given
        val topic = getTopic()
        val message = getMessage()
        every { userService.getActiveUserId() } returns 1
        every { topicService.findTopic(any()) } returns topic
        every { projectService.isInProject(any(), any()) } returns true
        every { messageRepository.save(any()) } returns message
        every { queueService.sendMessage(any(), any()) } throws QueueException()

        // throws
        assertThrows<QueueException> { messageService.sendMessage(message.toDTO()) }
    }

    @Test
    fun sendMessageWrongProject() {
        // given
        val topic = getTopic()
        val message = getMessage()
        every { userService.getActiveUserId() } returns 1
        every { topicService.findTopic(any()) } returns topic
        every { projectService.isInProject(any(), any()) } returns false

        // throws
        assertThrows<NotAllowedException> { messageService.sendMessage(message.toDTO()) }
    }

    @Test
    fun sendMessageFromDevicePositive() {
        // given
        val message = getMessage()
        val topic = getTopic()
        val messageDTO = getMessageDto()
        every { topicService.findTopic(any()) } returns topic
        every { messageRepository.save(any()) } returns message
        every { queueService.sendMessage(any(), any()) } returns Unit

        // when
        val actual = messageService.sendMessageFromDevice(messageDTO)

        // then
        assertEquals(messageDTO, actual)
    }

    @Test
    fun sendMessageFromDeviceWrongTopic() {
        // given
        val messageDTO = getMessageDto()
        every { topicService.findTopic(any()) } throws InvalidDataException()

        // throws
        assertThrows<InvalidDataException> { messageService.sendMessageFromDevice(messageDTO) }
    }

    @Test
    fun sendMessageFromDeviceWrongConnectionKey() {
        // given
        val topic = getTopic()
        val messageDTO = getMessageDto()
        messageDTO.connectionKey = "wrong"
        every { topicService.findTopic(any()) } returns topic

        // throws
        assertThrows<NotAllowedException> { messageService.sendMessageFromDevice(messageDTO) }
    }

    @Test
    fun sendMessageFromDeviceRabbitError() {
        // given
        val message = getMessage()
        val topic = getTopic()
        val messageDTO = getMessageDto()
        every { topicService.findTopic(any()) } returns topic
        every { messageRepository.save(any()) } returns message
        every { queueService.sendMessage(any(), any()) } throws QueueException()

        // throws
        assertThrows<QueueException> { messageService.sendMessageFromDevice(messageDTO) }
    }

    @Test
    fun getLastMessagesForDashboardPositive() {
        // given
        val last3Messages = getListOfMessages(start = 3, stop = 5)
        val dashboard = getDashboard()
        val topicMessageDTO = TopicMessagesDTO(1, last3Messages.map { it.toDTO() })
        every { userService.getActiveUserId() } returns 1
        every { dashboardService.findById(any()) } returns dashboard.toDTO()
        every { projectService.isInProject(any(), any()) } returns true
        every { componentService.findAllByDashboardId(any()) } returns getComponents()
        every { messageRepository.findNLastMessagesForTopics(any(), any()) } returns last3Messages

        // when
        val actual = messageService.getLastMessagesForDashboard(1, 3)

        // then
        assertEquals(actual, listOf(topicMessageDTO))
    }

    @Test
    fun getLastMessagesUserNotAuthenticated() {
        // given
        every { userService.getActiveUserId() } returns null

        // throws
        assertThrows<NoAuthenticationException> { messageService.getLastMessagesForDashboard(1, 1) }
    }

    @Test
    fun getLastMessagesUserNotInProject() {
        // given
        val dashboard = getDashboard()
        every { userService.getActiveUserId() } returns 1
        every { dashboardService.findById(any()) } returns dashboard.toDTO()
        every { projectService.isInProject(any(), any()) } returns false

        // throws
        assertThrows<NotAllowedException> { messageService.getLastMessagesForDashboard(1, 1) }
    }
}