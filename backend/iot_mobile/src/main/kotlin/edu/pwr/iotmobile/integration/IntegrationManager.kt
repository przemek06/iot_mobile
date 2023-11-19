package edu.pwr.iotmobile.integration

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class IntegrationManager {

    val integrationActionSet: MutableSet<IntegrationAction> = ConcurrentHashMap.newKeySet()

    fun addIntegrationAction() {}

    fun removeIntegrationAction(){}
}