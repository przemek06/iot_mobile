package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.MailMessageDTO
import edu.pwr.iotmobile.service.IntegrationService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

// will be called from another service
@RestController
class IntegrationController(val integrationService: IntegrationService) {

    @PostMapping("/integration/gmail")
    fun sendMailMessage(@RequestBody dto: MailMessageDTO): ResponseEntity<Unit> {
        return ResponseEntity.ok(integrationService.sendMailMessage(dto))
    }
}