package edu.pwr.iotmobile.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class EmailDTO(
    @Email
    @NotNull
    val address: String
)
