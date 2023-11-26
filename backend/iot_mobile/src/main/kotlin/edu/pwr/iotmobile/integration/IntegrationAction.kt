package edu.pwr.iotmobile.integration

import edu.pwr.iotmobile.dto.MessageDTO

interface IntegrationAction {
    fun performAction(data: MessageDTO)
}