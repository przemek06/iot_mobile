package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.enums.ERole

data class UserInfoDTO(
    val id: Int,
    val email: String,
    val role: ERole,
    val name: String,
    val isBlocked: Boolean,
    val isActive: Boolean
) {
    fun toEntity(role: ERole = ERole.USER_ROLE) : User {
        return User(email, "", role, name, isBlocked, isActive, id)
    }
}
