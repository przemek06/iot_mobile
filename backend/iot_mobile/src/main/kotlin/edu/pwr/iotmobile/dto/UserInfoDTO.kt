package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.security.Role
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UserInfoDTO(
    val id: Int,
    val email: String,
    val role: Role,
    val name: String,
    val isBlocked: Boolean,
    val isActive: Boolean
) {
    fun toEntity(role: Role = Role.USER_ROLE) : User {
        return User(email, "", role, name, isBlocked, isActive, id)
    }
}
