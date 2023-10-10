package edu.pwr.iotmobile.dto

import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.security.Role
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UserDTO(
    @field:NotNull
    @field:Email
    val email: String,
    @field:NotNull
    @field:Size(min = 8, max = 64)
    val password: String,
    @field:NotNull
    @field:Size(min = 3, max = 64)
    val name: String,

) {
    fun toEntity(role: Role = Role.USER_ROLE) : User {
        return User(email, password, role, name)
    }
}