package edu.pwr.iotmobile.androidimcs.data.dto

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
    }
}
