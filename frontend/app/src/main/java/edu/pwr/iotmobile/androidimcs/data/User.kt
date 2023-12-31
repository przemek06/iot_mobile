package edu.pwr.iotmobile.androidimcs.data

import edu.pwr.iotmobile.androidimcs.UserStore
import edu.pwr.iotmobile.androidimcs.data.dto.UserInfoDto
import edu.pwr.iotmobile.androidimcs.helpers.extensions.asEnum

data class User(
    val id: Int,
    val displayName: String,
    val email: String,
    val role: UserRole,
    val isBlocked: Boolean = false,
    val isActive: Boolean = false,
) {
    companion object {
        fun UserStore.toUser(): User? {
            return User(
                id = id,
                displayName = name,
                email = email,
                role = role.asEnum<UserRole>() ?: return null,
                isBlocked = isBlocked,
                isActive = isActive
            )
        }
        fun UserInfoDto.toUser(): User? {
            return User(
                id = id,
                displayName = name,
                email = email,
                role = role.asEnum<UserRole>() ?: return null,
                isBlocked = isBlocked,
                isActive = isActive
            )
        }
    }
}

enum class UserRole {
    USER_ROLE,
    ADMIN_ROLE
}

enum class UserProjectRole {
    VIEWER,
    EDITOR,
    ADMIN
}