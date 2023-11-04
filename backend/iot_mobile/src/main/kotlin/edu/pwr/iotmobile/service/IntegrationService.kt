package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.MailMessageDTO
import org.springframework.stereotype.Service


// Will be called from the outside Trigger Service
@Service
class IntegrationService(
    val mailService: MailService
) {

    fun sendMailMessage(dto: MailMessageDTO) {
        val content = String.format(dto.content, dto.data)
        mailService.sendPlainTextMail(dto.subject, dto.recipient, content)
    }
}