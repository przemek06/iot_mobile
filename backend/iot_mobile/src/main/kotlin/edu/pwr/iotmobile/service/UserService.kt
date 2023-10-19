package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.PasswordDTO
import edu.pwr.iotmobile.dto.UserDTO
import edu.pwr.iotmobile.dto.UserInfoDTO
import edu.pwr.iotmobile.error.exception.TokenCodeIncorrectException
import edu.pwr.iotmobile.error.exception.UserAlreadyExistsException
import edu.pwr.iotmobile.error.exception.UserNotFoundException
import edu.pwr.iotmobile.repositories.UserRepository
import edu.pwr.iotmobile.enums.ERole
import edu.pwr.iotmobile.security.UserDetailsImpl
import org.springframework.beans.BeanUtils
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserService(
    val userRepository: UserRepository,
    val mailService: MailService,
    val verificationTokenService: VerificationTokenService,
    val resetPasswordTokenService: ResetPasswordTokenService,
    val passwordEncoder: PasswordEncoder
) {

    fun userExists(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    fun createUser(userDTO: UserDTO, role: ERole) : UserDTO {
        if (userExists(userDTO.email)) {
            throw UserAlreadyExistsException()
        }

        val toSave = userDTO.toEntity(role = role)
        toSave.password = passwordEncoder.encode(toSave.password)
        toSave.isActive = true
        val saved = userRepository.save(toSave)
        return saved.toUserDTO()
    }

    @Transactional
    fun registerUser(userDTO: UserDTO): UserDTO {
        if (userExists(userDTO.email)) {
            throw UserAlreadyExistsException()
        }

        val toSave = userDTO.toEntity()
        toSave.password = passwordEncoder.encode(toSave.password)

        val saved = userRepository.save(toSave)

        val token = verificationTokenService.createVerificationToken(saved)
        mailService.sendUserVerificationMail(saved, token.code)

        return saved.toUserDTO()
    }

    fun verifyUser(token: String) : Boolean {
        val verificationToken = verificationTokenService.findActiveByCode(token)
        val user = verificationToken.user

        if (!user.isActive) {
            user.isActive = true
            userRepository.save(user)
            return true
        }

        return false
    }

    private fun getAuthentication(): Authentication {
        return SecurityContextHolder.getContext().authentication
    }

    fun getActiveUserId() : Int {
        val authentication: Authentication = getAuthentication()

        if (authentication.principal is UserDetailsImpl) {
            return (authentication.principal as UserDetailsImpl).getId() ?: throw UserNotFoundException()
        }

        throw UserNotFoundException()
    }

    fun deleteUserById(id: Int) {
        userRepository.deleteById(id)
    }

    fun toggleBlockedById(id: Int) : UserInfoDTO {
        val user = userRepository.findUserById(id) ?: throw UserNotFoundException()
        user.isBlocked = !user.isBlocked
        return userRepository.save(user).toUserInfoDTO()
    }

    fun changeUserRole(id: Int, role: ERole) : UserInfoDTO {
        val user = userRepository.findUserById(id) ?: throw UserNotFoundException()
        user.role = role
        return userRepository.save(user).toUserInfoDTO()
    }

    private fun updateUser(id: Int, user: UserDTO) : UserDTO {
        val toUpdate = userRepository.findUserById(id) ?: throw UserNotFoundException()
        val password = toUpdate.password
        BeanUtils.copyProperties(user, toUpdate)
        toUpdate.password = password
        return userRepository.save(toUpdate).toUserDTO()
    }

    private fun updateUserPassword(id: Int, passwordDTO: PasswordDTO) : UserDTO {
        val toUpdate = userRepository.findUserById(id) ?: throw UserNotFoundException()
        toUpdate.password = passwordEncoder.encode(passwordDTO.password)
        return userRepository.save(toUpdate).toUserDTO()
    }

    fun updateActiveUserPassword(passwordDTO: PasswordDTO) : UserDTO {
        val id = getActiveUserId()
        return updateUserPassword(id, passwordDTO)
    }

    fun updateActiveUser(user: UserDTO) : UserDTO {
        val id = getActiveUserId()
        return updateUser(id, user)
    }

    fun deleteActiveUser() {
        val id = getActiveUserId()
        userRepository.deleteById(id)
    }

    fun getAllUserInfo(): List<UserInfoDTO> {
        return userRepository
            .findAll()
            .map { it.toUserInfoDTO() }
    }

    fun getUserInfoById(id: Int) : UserInfoDTO {
        val user = userRepository.findUserById(id) ?: throw UserNotFoundException()
        return user.toUserInfoDTO()
    }

    fun getActiveUserInfo() : UserInfoDTO {
        val id = getActiveUserId()
        return getUserInfoById(id)
    }

    @Transactional
    fun startResetPassword(email: String) {
        val user = userRepository.findUserByEmail(email) ?: throw UserNotFoundException()

        val resetPasswordToken = resetPasswordTokenService.createVerificationToken(user)

        mailService.sendResetPasswordMail(user, resetPasswordToken.code)
    }

    @Transactional
    fun resetPassword(email: String, code: String, passwordDTO: PasswordDTO) : UserDTO {
        val user = userRepository.findUserByEmail(email) ?: throw UserNotFoundException()
        val id = user.id ?: throw UserNotFoundException()
        if (resetPasswordTokenService.isCodeCorrect(id, code)) {
            resetPasswordTokenService.deactivateUserTokens(id)
            return updateUserPassword(id, passwordDTO)
        }
        throw TokenCodeIncorrectException()
    }
}