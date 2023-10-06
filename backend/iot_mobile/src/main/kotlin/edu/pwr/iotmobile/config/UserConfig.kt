package edu.pwr.iotmobile.config

import edu.pwr.iotmobile.dto.UserDTO
import edu.pwr.iotmobile.security.Role
import edu.pwr.iotmobile.service.UserService
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfig(
    private val userService: UserService
) {

    @PostConstruct
    fun init() {
        val admin = UserDTO("admin@gmail.com", "testtest", "Jon Doe")
        val user = UserDTO("user@gmail.com", "testtest", "James Doe")
        userService.createUser(admin, Role.ADMIN_ROLE)
        userService.createUser(user, Role.USER_ROLE)
    }

}