package edu.pwr.iotmobile.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class PasswordDTO(
    @field:NotNull
    @field:Size(min = 8, max = 64)
    val password: String
    )
