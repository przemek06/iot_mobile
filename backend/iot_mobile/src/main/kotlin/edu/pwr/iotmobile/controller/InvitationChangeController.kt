package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.service.InvitationChangeService
import kotlinx.coroutines.flow.Flow
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class InvitationChangeController(
        val invitationChangeService: InvitationChangeService
) {
    @MessageMapping("invitationChange")
    fun requestResponse(): Flow<Boolean> = invitationChangeService.getInvitationFlow()
}