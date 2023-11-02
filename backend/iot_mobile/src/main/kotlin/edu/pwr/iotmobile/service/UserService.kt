package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.EmailDTO
import edu.pwr.iotmobile.dto.PasswordDTO
import edu.pwr.iotmobile.dto.UserDTO
import edu.pwr.iotmobile.dto.UserInfoDTO
import edu.pwr.iotmobile.enums.ERole
import edu.pwr.iotmobile.error.exception.NoAuthenticationException
import edu.pwr.iotmobile.error.exception.TokenCodeIncorrectException
import edu.pwr.iotmobile.error.exception.UserAlreadyExistsException
import edu.pwr.iotmobile.error.exception.UserNotFoundException
import edu.pwr.iotmobile.repositories.UserRepository
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

    fun userExistsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    fun userExistsById(id: Int): Boolean {
        return userRepository.existsById(id)
    }

    fun createUser(userDTO: UserDTO, role: ERole) : UserDTO {
        if (userExistsByEmail(userDTO.email)) {
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
        if (userExistsByEmail(userDTO.email)) {
            throw UserAlreadyExistsException()
        }

        val toSave = userDTO.toEntity()
        toSave.password = passwordEncoder.encode(toSave.password)

        val saved = userRepository.save(toSave)

        val token = verificationTokenService.createVerificationToken(saved)
        mailService.sendUserVerificationMail(saved, token.code)

        return saved.toUserDTO()
    }

    @Transactional
    fun resendVerificationCode(email: EmailDTO) {
        val user = userRepository.findUserByEmail(email.address) ?: throw UserNotFoundException()
        val token = verificationTokenService.createVerificationToken(user)
        mailService.sendUserVerificationMail(user, token.code)
    }

    @Transactional
    fun verifyUser(token: String) : Boolean {
        val verificationToken = verificationTokenService.findActiveByCode(token)
        val user = verificationToken.user
        verificationTokenService.deleteAllByUserId(user.id ?: throw UserNotFoundException())

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

    fun getActiveUserId() : Int? {
        val authentication: Authentication = getAuthentication()

        if (authentication.principal is UserDetailsImpl) {
            return (authentication.principal as UserDetailsImpl).getId()
        }

        return null
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

        if ((userExistsByEmail(user.email)) and (toUpdate.email != user.email)) {
            throw UserAlreadyExistsException()
        }

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
        val id = getActiveUserId() ?: throw NoAuthenticationException()
        return updateUserPassword(id, passwordDTO)
    }

    fun updateActiveUser(user: UserDTO) : UserDTO {
        val id = getActiveUserId() ?: throw NoAuthenticationException()
        return updateUser(id, user)
    }

    fun deleteActiveUser() : Unit {
        val id = getActiveUserId() ?: throw NoAuthenticationException()
        deleteUserById(id)
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
        val id = getActiveUserId() ?: throw NoAuthenticationException()
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