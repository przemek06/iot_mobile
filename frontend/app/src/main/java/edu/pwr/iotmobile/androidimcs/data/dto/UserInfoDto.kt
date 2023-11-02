package edu.pwr.iotmobile.androidimcs.data.dto

import edu.pwr.iotmobile.androidimcs.data.User
import edu.pwr.iotmobile.androidimcs.data.UserRole

data class UserInfoDto(
    val id: Int,
    val email: String,
    val role: String,
    val name: String,
    val isBlocked: Boolean,
    val isActive: Boolean
) {
    companion object {
        fun UserInfoDto.toUser(): User {
            return User(
                id = this.id,
                displayName = this.name,
                email = this.name,
                role = UserRole.valueOf(this.role),
                isBlocked = this.isBlocked,
                isActive = isActive
            )
        }
    }
}
