package edu.pwr.iotmobile.entities

import edu.pwr.iotmobile.dto.UserDTO
import edu.pwr.iotmobile.dto.UserInfoDTO
import edu.pwr.iotmobile.security.Role
import jakarta.persistence.*

@Entity(name = "_user")
class User (
    @Column(nullable = false)
    var email: String,
    @Column(nullable = false)
    var password: String,
    @Column(nullable = false)
    var role: Role,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var isBlocked: Boolean = false,
    @Column(nullable = false)
    var isActive: Boolean = false,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
    ) {
    constructor() : this("", "", Role.USER_ROLE, "", false, false)

    fun toUserDTO() : UserDTO {
        return UserDTO(email, password, name)
    }

    fun toUserInfoDTO() : UserInfoDTO {
        return UserInfoDTO(id!!, email, role, name, isBlocked, isActive)
    }

}