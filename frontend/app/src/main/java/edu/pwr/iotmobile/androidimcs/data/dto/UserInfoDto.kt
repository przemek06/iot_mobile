package edu.pwr.iotmobile.androidimcs.data.dto

import edu.pwr.iotmobile.androidimcs.data.User

data class UserInfoDto(
    val id: Int,
    val email: String,
    val role: String,
    val name: String,
    val isBlocked: Boolean,
    val isActive: Boolean
) {
    companion object {
        fun empty() = UserInfoDto(
            id = 0,
            email = "",
            role = "",
            name = "",
            isBlocked = false,
            isActive = false
        )

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
