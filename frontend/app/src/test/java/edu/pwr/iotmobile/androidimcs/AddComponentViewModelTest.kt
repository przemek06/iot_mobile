@file:OptIn(ExperimentalCoroutinesApi::class)

package edu.pwr.iotmobile.androidimcs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.pwr.iotmobile.androidimcs.data.ComponentDetailedType
import edu.pwr.iotmobile.androidimcs.data.TopicDataType
import edu.pwr.iotmobile.androidimcs.data.dto.TopicDto
import edu.pwr.iotmobile.androidimcs.data.ui.Topic
import edu.pwr.iotmobile.androidimcs.helpers.event.Event
import edu.pwr.iotmobile.androidimcs.helpers.toast.Toast
import edu.pwr.iotmobile.androidimcs.model.repository.ComponentRepository
import edu.pwr.iotmobile.androidimcs.model.repository.IntegrationRepository
import edu.pwr.iotmobile.androidimcs.model.repository.TopicRepository
import edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent.AddComponentPage
import edu.pwr.iotmobile.androidimcs.ui.screens.addcomponent.AddComponentViewModel
import edu.pwr.iotmobile.androidimcs.utils.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class AddComponentViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainCoroutineRule = MainDispatcherRule()

    private val topicRepository: TopicRepository = mockk()
    private val componentRepository: ComponentRepository = mockk()
    private val integrationRepository: IntegrationRepository = mockk()
    private val event: Event = mockk()
    private val toast: Toast = mockk()
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main

    private val viewModel = AddComponentViewModel(
        topicRepository = topicRepository,
        componentRepository = componentRepository,
        integrationRepository = integrationRepository,
        event = event,
        toast = toast,
        dispatcher = dispatcher
    )

    @Test
    fun initSuccessTest() {
        viewModel.init(1)

        assertFalse { viewModel.uiState.value.isLoading }
        assertFalse { viewModel.uiState.value.isError }
        assertTrue { viewModel.uiState.value.inputComponents.isNotEmpty() }
        assertTrue { viewModel.uiState.value.outputComponents.isNotEmpty() }
        assertTrue { viewModel.uiState.value.triggerComponents.isNotEmpty() }
    }

    @Test
    fun initialNavigationTest() {
        everyToastReturns()
        everyGetTopicsReturns()

        viewModel.init(1)

        assertEquals(AddComponentPage.ChooseComponent, viewModel.uiState.value.currentPage)

        viewModel.navigateNext("")

        assertEquals(AddComponentPage.ChooseComponent, viewModel.uiState.value.currentPage)

        viewModel.onChooseComponent(getDefaultComponentChoiceData())
        viewModel.navigateNext("")

        assertEquals(AddComponentPage.ChooseTopic, viewModel.uiState.value.currentPage)
        assertEquals(ComponentDetailedType.ReleaseButton, viewModel.uiState.value.chosenComponentType)
    }

    @Test
    fun chooseTopicNavigationFailureTest() {
        everyToastReturns()
        everyGetTopicsReturns()

        viewModel.init(1)

        viewModel.onChooseComponent(getDefaultComponentChoiceData())
        viewModel.navigateNext("")

        viewModel.navigateNext("")

        assertNotEquals(AddComponentPage.Settings, viewModel.uiState.value.currentPage)
    }

    @Test
    fun chooseTopicNavigationSuccessTest() {
        everyToastReturns()
        everyGetTopicsReturns()

        viewModel.init(1)

        viewModel.onChooseComponent(getDefaultComponentChoiceData())
        viewModel.navigateNext("")

        val topic = getDefaultTopic()
        viewModel.onChooseTopic(topic)
        viewModel.navigateNext("")

        assertEquals(AddComponentPage.Settings, viewModel.uiState.value.currentPage)
        assertEquals(topic, viewModel.uiState.value.chosenTopic)
    }

    private fun everyToastReturns() {
        every {
            runBlocking { toast.toast(any()) }
        } returns Unit
    }

    private fun everyGetTopicsReturns() {
        every {
            runBlocking { topicRepository.getTopicsByProjectId(any()) }
        } returns listOf(getDefaultTopicDto())
    }

    private fun getDefaultTopicDto() = TopicDto(
        id = 1,
        projectId = 1,
        name = "t1",
        uniqueName = "t1",
        valueType = TopicDataType.INT
    )

    private fun getDefaultComponentChoiceData() = AddComponentViewModel.ComponentChoiceData(
        titleId = R.string.add_component,
        iconRes = R.drawable.ic_button,
        type = ComponentDetailedType.ReleaseButton
    )

    private fun getDefaultTopic() = Topic(
        id = 1,
        title = "t1",
        projectId = 1,
        name = "t1",
        dataType = TopicDataType.INT
    )
}