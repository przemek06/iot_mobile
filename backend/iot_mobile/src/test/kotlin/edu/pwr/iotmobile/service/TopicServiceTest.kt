package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.entities.Topic
import edu.pwr.iotmobile.error.exception.NoAuthenticationException
import edu.pwr.iotmobile.error.exception.NotAllowedException
import edu.pwr.iotmobile.error.exception.TopicAlreadyExistsException
import edu.pwr.iotmobile.error.exception.TopicUsedException
import edu.pwr.iotmobile.rabbit.queue.QueueService
import edu.pwr.iotmobile.repositories.TopicRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*


class TopicServiceTest {

    private val topicRepository = mockk<TopicRepository>()
    private val userService = mockk<UserService>()
    private val projectService = mockk<ProjectService>()
    private val queueService = mockk<QueueService>()
    private val topicService = TopicService(
        topicRepository = topicRepository,
        userService = userService,
        projectService = projectService,
        queueService = queueService
    )

    private fun getTopic(): Topic {
        val topic = Topic()
        topic.project.id = 1
        return topic
    }

    @Test
    fun createTopicPositive() {
        // given
        val topic = getTopic()
        every { userService.getActiveUserId() } returns 1
        every { projectService.isEditor(any(), any()) } returns true
        every { topicRepository.existsByUniqueNameAndProjectId(any(), any()) } returns false
        every { queueService.addExchange(any()) } returns Unit
        every { topicRepository.save(any()) } returns topic

        // when
        val actual = topicService.createTopic(topic.toDTO())

        // then
        assertEquals(actual, topic.toDTO())
    }

    @Test
    fun createTopicExists() {
        // given
        val topic = getTopic()
        every { userService.getActiveUserId() } returns 1
        every { projectService.isEditor(any(), any()) } returns true
        every { topicRepository.existsByUniqueNameAndProjectId(any(), any()) } returns true

        // throws
        assertThrows<TopicAlreadyExistsException> { topicService.createTopic(topic.toDTO()) }
    }

    @Test
    fun createTopicENotAllowed() {
        // given
        val topic = getTopic()
        every { userService.getActiveUserId() } returns 1
        every { projectService.isEditor(any(), any()) } returns false

        // throws
        assertThrows<NotAllowedException> { topicService.createTopic(topic.toDTO()) }
    }

    @Test
    fun createTopicUserNotAuthenticated() {
        // given
        val topic = getTopic()
        every { userService.getActiveUserId() } returns null

        // throws
        assertThrows<NoAuthenticationException> { topicService.createTopic(topic.toDTO()) }
    }

    @Test
    fun deleteTopicPositive() {
        // given
        val topic = getTopic()
        every { userService.getActiveUserId() } returns 1
        every { topicRepository.findById(any()) } returns Optional.of(topic)
        every { projectService.isEditor(any(), any()) } returns true
        every { topicRepository.countTopicUsage(any()) } returns 0
        every { topicRepository.delete(any()) } returns Unit
        every { queueService.forceDeleteExchange(any()) } returns Unit

        // when
        val actual = topicService.deleteTopic(1)

        //then
        assertEquals(true, actual)
    }

    @Test
    fun deleteTopicUsed() {
        // given
        val topic = getTopic()
        every { userService.getActiveUserId() } returns 1
        every { topicRepository.findById(any()) } returns Optional.of(topic)
        every { projectService.isEditor(any(), any()) } returns true
        every { topicRepository.countTopicUsage(any()) } returns 1

        // throws
        assertThrows<TopicUsedException> { topicService.deleteTopic(1) }
    }

    @Test
    fun deleteTopicNotAllowed() {
        // given
        val topic = getTopic()
        every { userService.getActiveUserId() } returns 1
        every { topicRepository.findById(any()) } returns Optional.of(topic)
        every { projectService.isEditor(any(), any()) } returns false

        // throws
        assertThrows<NotAllowedException> { topicService.deleteTopic(1) }
    }

    @Test
    fun deleteTopicNotAuthenticated() {
        // given
        every { userService.getActiveUserId() } returns null

        //throws
        assertThrows<NoAuthenticationException> { topicService.deleteTopic(1) }
    }

}