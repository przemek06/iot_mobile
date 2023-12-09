package edu.pwr.iotmobile.integration

import com.fasterxml.jackson.databind.ObjectMapper
import edu.pwr.iotmobile.entities.TriggerComponent
import edu.pwr.iotmobile.enums.EActionDestinationType
import edu.pwr.iotmobile.error.exception.InvalidStateException
import edu.pwr.iotmobile.rabbit.RabbitListener
import edu.pwr.iotmobile.service.ComponentService
import edu.pwr.iotmobile.service.MailService
import edu.pwr.iotmobile.service.NotificationService
import edu.pwr.iotmobile.service.ProjectService
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Component
import reactor.core.Disposable
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.ConcurrentHashMap

@Component
class IntegrationManager(
    private val discordBot: DiscordBot,
    private val slackBot: SlackBot,
    private val telegramBot: TelegramBot,
    private val mailService: MailService,
    private val projectService: ProjectService,
    private val componentService: ComponentService,
    private val rabbitListener: RabbitListener,
    private val notificationService: NotificationService
) {
    class IntegrationWrapper(
        val integrationAction: IntegrationAction,
        val consumerTag: String,
        val subscription: Disposable
    )

    val integrationActionMap: MutableMap<Int, IntegrationWrapper> = ConcurrentHashMap()
    val objectMapper = ObjectMapper()

    @PostConstruct
    fun loadIntegrations() {
        val triggerComponents = componentService.findAllTriggerComponents()

        triggerComponents.forEach { addIntegrationAction(it, it.topic.project.connectionKey) }
    }

    @PreDestroy
    fun clearIntegrations() {
        integrationActionMap.values
            .forEach {
                it.subscription.dispose()
                rabbitListener.cancelConsumer(it.consumerTag)
            }
    }

    fun addIntegrationAction(component: TriggerComponent, connectionKey: String) {
        if (component.actionDestination.type == EActionDestinationType.DISCORD) {
            val integrationAction = createDiscordIntegrationAction(component)
            addIntegrationAction(component, integrationAction, connectionKey)
        } else if (component.actionDestination.type == EActionDestinationType.EMAIL) {
            val integrationAction = createMailIntegrationAction(component)
            addIntegrationAction(component, integrationAction, connectionKey)
        } else if (component.actionDestination.type == EActionDestinationType.NOTIFICATION) {
            val integrationAction = createNotificationIntegrationAction(component)
            addIntegrationAction(component, integrationAction, connectionKey)
        } else if (component.actionDestination.type == EActionDestinationType.SLACK) {
            val integrationAction = createSlackIntegrationAction(component)
            addIntegrationAction(component, integrationAction, connectionKey)
        } else if (component.actionDestination.type == EActionDestinationType.TELEGRAM) {
            val integrationAction = createTelegramIntegrationAction(component)
            addIntegrationAction(component, integrationAction, connectionKey)
        }
    }

    private fun addIntegrationAction(
        component: TriggerComponent,
        integrationAction: IntegrationAction,
        connectionKey: String
    ) {
        val tagFlux = rabbitListener.registerConsumer(component.topic.uniqueName, connectionKey)
        val subscription = tagFlux.second.subscribe { integrationAction.performAction(it) }
        val wrapper = IntegrationWrapper(integrationAction, tagFlux.first, subscription)

        component.id?.let { integrationActionMap.put(it, wrapper) }
    }

    private fun createDiscordIntegrationAction(component: TriggerComponent): DiscordIntegrationAction {
        return DiscordIntegrationAction(discordBot, component.actionDestination.token, component.pattern)
    }

    private fun createMailIntegrationAction(component: TriggerComponent): EmailIntegrationAction {
        return EmailIntegrationAction(
            mailService,
            projectService,
            component.topic.project.id ?: throw InvalidStateException(),
            component.pattern,
            component.actionDestination.token
        )
    }

    private fun createNotificationIntegrationAction(component: TriggerComponent): NotificationIntegrationAction {
        return NotificationIntegrationAction(projectService, notificationService, component)
    }

    fun removeIntegrationAction(componentId: Int) {
        val wrapper = integrationActionMap.remove(componentId) ?: return
        rabbitListener.cancelConsumer(wrapper.consumerTag)
        wrapper.subscription.dispose()
    }

    private fun createSlackIntegrationAction(component: TriggerComponent): SlackIntegrationAction {
        return SlackIntegrationAction(component.actionDestination.token, slackBot)
    }

    private fun createTelegramIntegrationAction(component: TriggerComponent): TelegramIntegrationAction {
        if(getTelegramChatId(component).isBlank()){
            component.actionDestination.token = updateTelegramToken(component)
        }
        return TelegramIntegrationAction(component.actionDestination.token, telegramBot, component.pattern)
    }

    fun updateTelegramToken(component: TriggerComponent): String{
        return "${requestChatId(component)};${getTelegramBotToken(component)}"
    }

    fun requestChatId(component: TriggerComponent): String{
        val botToken = getTelegramBotToken(component)
        val uri = "https://api.telegram.org/bot$botToken/getUpdates"
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val chatInfo = objectMapper.readTree(response.body())
        val chatId = chatInfo.get("result")[0]["my_chat_member"]["chat"]["id"]
        return chatId.asText()
    }

    fun getTelegramChatId(component: TriggerComponent): String{
        return component.actionDestination.token.split(";")[0]
    }

    fun getTelegramBotToken(component: TriggerComponent): String{
        return component.actionDestination.token.split(";")[1]
    }
}