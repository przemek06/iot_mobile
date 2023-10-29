package edu.pwr.iotmobile.controller

import edu.pwr.iotmobile.dto.EmailDTO
import edu.pwr.iotmobile.dto.PasswordDTO
import edu.pwr.iotmobile.dto.UserDTO
import edu.pwr.iotmobile.dto.UserInfoDTO
import edu.pwr.iotmobile.enums.ERole
import edu.pwr.iotmobile.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class UserController(val userService: UserService) {

    // anon endpoints

    // 400, 409
    @PostMapping("/anon/users")
    fun registerUser(@Valid @RequestBody user: UserDTO): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.registerUser(user))
    }

    // 400, 404
    @PostMapping("/anon/users/verification/resend")
    fun resendVerificationCode(@RequestBody email: EmailDTO) : ResponseEntity<Unit> {
        return ResponseEntity.ok(userService.resendVerificationCode(email))
    }

    // 404
    @GetMapping("/anon/users/info/{id}")
    fun getUserInfoById(@PathVariable id: Int) : ResponseEntity<UserInfoDTO> {
        return ResponseEntity.ok(userService.getUserInfoById(id))
    }

    // -
    @GetMapping("/anon/users/info")
    fun getAllUserInfo() : ResponseEntity<List<UserInfoDTO>> {
        return ResponseEntity.ok(userService.getAllUserInfo())
    }

    // 404
    @PostMapping("/anon/users/verify/{code}")
    fun verifyUser(@PathVariable code: String) : ResponseEntity<Unit> {
        return if (userService.verifyUser(code)) ResponseEntity.ok().build()
        else ResponseEntity.noContent().build()
    }

    // 404
    @PostMapping("/anon/users/reset/{email}")
    fun startResetPassword(@PathVariable email: String) : ResponseEntity<Unit> {
        return ResponseEntity.ok(userService.startResetPassword(email))
    }

    // 300, 403, 404
    @PostMapping("/anon/users/reset/{email}/{code}")
    fun resetPassword(@PathVariable email: String, @PathVariable code: String, @RequestBody @Valid passwordDTO: PasswordDTO) : ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.resetPassword(email, code, passwordDTO))
    }

    // user endpoints

    // 401, 403, 404
    @GetMapping("/user/users/info")
    fun getActiveUserInfo() : ResponseEntity<UserInfoDTO> {
        return ResponseEntity.ok(userService.getActiveUserInfo())
    }

    // 400, 401, 403, 404
    @PutMapping("/user/users")
    fun updateActiveUser(@Valid @RequestBody user: UserDTO) : ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.updateActiveUser(user))
    }

    // 400, 401, 403, 404
    @PutMapping("/user/users/password")
    fun updateActiveUserPassword(@Valid @RequestBody passwordDTO: PasswordDTO) : ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.updateActiveUserPassword(passwordDTO))
    }

    // 401, 403 TODO: delete user from session
    @DeleteMapping("/user/users")
    fun deleteActiveUser() : ResponseEntity<Unit> {
        return ResponseEntity.ok(userService.deleteActiveUser())
    }

    // admin endpoints

    // 403 TODO: delete user from session
    @DeleteMapping("/admin/users/{id}")
    fun deleteUserById(@PathVariable id: Int) : ResponseEntity<Unit> {
        return ResponseEntity.ok(userService.deleteUserById(id))
    }

    // 400, 403, 404, 409
    @PutMapping("/admin/users/role/{id}/{role}")
    fun updateUserRole(@PathVariable id: Int, @PathVariable role: ERole) : ResponseEntity<UserInfoDTO> {
        return ResponseEntity.ok(userService.changeUserRole(id, role))
    }


    // 403, 404 TODO: delete blocked user from session
    @PutMapping("/admin/users/block/toggle/{id}")
    fun toggleUserBlocked(@PathVariable id: Int) : ResponseEntity<UserInfoDTO> {
        return ResponseEntity.ok(userService.toggleBlockedById(id))
    }


}