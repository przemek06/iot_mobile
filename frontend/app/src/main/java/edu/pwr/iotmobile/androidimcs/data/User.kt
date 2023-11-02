package edu.pwr.iotmobile.androidimcs.data

import edu.pwr.iotmobile.androidimcs.data.dto.UserDto
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto

data class User(
    val id: Int,
    val displayName: String,
    val email: String,
    val role: UserRole,
    val isBlocked: Boolean = false,
    val isActive: Boolean = false
) {
    companion object {
        fun User.toDto(): UserInfoDto {
            return UserInfoDto(
                id = this.id,
                email = this.email,
                role = this.role.name,
                name = this.displayName,
                isBlocked = this.isBlocked,
                isActive = this.isActive
            )
        }
    }
}

enum class UserRole {
    Normal,
    Admin
}

enum class UserProjectRole {
    View,
    Editor,
    Admin
}