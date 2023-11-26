package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.entities.TriggerComponent
import edu.pwr.iotmobile.enums.EActionDestinationType
import edu.pwr.iotmobile.error.exception.InvalidStateException
import edu.pwr.iotmobile.rabbit.RabbitListener
import edu.pwr.iotmobile.service.ComponentService
import edu.pwr.iotmobile.service.MailService
import edu.pwr.iotmobile.service.NotificationService
import edu.pwr.iotmobile.service.ProjectService
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import reactor.core.Disposable
import java.util.concurrent.ConcurrentHashMap

@Component
class IntegrationManager(
    private val discordBot: DiscordBot,
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

    @PostConstruct
    fun loadIntegrations() {
        val triggerComponents = componentService.findAllTriggerComponents()

        triggerComponents.forEach { addIntegrationAction(it, it.topic.project.connectionKey) }
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
        return NotificationIntegrationAction(projectService, notificationService)
    }

    fun removeIntegrationAction(componentId: Int) {
        val wrapper = integrationActionMap.remove(componentId) ?: return
        rabbitListener.cancelConsumer(wrapper.consumerTag)
        wrapper.subscription.dispose()
    }
}