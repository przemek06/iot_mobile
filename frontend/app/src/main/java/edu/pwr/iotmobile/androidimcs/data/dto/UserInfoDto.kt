package edu.pwr.iotmobile.androidimcs.data.dto

import androidx.compose.ui.semantics.Role

data class UserInfoDto(
    val id: Int,
    val email: String,
    val role: Role,
    val name: String,
    val isBlocked: Boolean,
    val isActive: Boolean
)
