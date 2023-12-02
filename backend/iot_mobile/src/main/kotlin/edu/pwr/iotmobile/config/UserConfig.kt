package edu.pwr.iotmobile.config

import edu.pwr.iotmobile.dto.UserDTO
import edu.pwr.iotmobile.enums.ERole
import edu.pwr.iotmobile.service.UserService
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfig(
    private val userService: UserService
) {

    @PostConstruct
    fun init() {
        try {
            val admin = UserDTO("admin@gmail.com", "testtest", "Jon Doe")
            val user = UserDTO("user@gmail.com", "testtest", "James Doe")
            userService.createUser(admin, ERole.ADMIN_ROLE)
            userService.createUser(user, ERole.USER_ROLE)
        } catch (e: Exception) {
            // do nothing
        }
    }

}