package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.dto.PasswordDTO
import edu.pwr.iotmobile.dto.UserDTO
import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.entities.VerificationToken
import edu.pwr.iotmobile.error.exception.TokenCodeIncorrectException
import edu.pwr.iotmobile.error.exception.UserAlreadyExistsException
import edu.pwr.iotmobile.error.exception.UserNotFoundException
import edu.pwr.iotmobile.repositories.UserRepository
import edu.pwr.iotmobile.enums.ERole
import edu.pwr.iotmobile.security.UserDetailsImpl
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

private const val EXPIRATION = 1L
private const val VALID_CODE_STRING = "000000"
private val VALID_EXPIRATION_DATE = Date.from(Instant.now().plus(EXPIRATION, ChronoUnit.HOURS))
private const val TOKEN_ID = 100
private const val EMAIL = "test@gmail.com"
private const val NOT_ENCODED_PASSWORD = "test"
private const val NOT_ENCODED_CHANGED_PASSWORD = "test2"
private const val ENCODED_PASSWORD = "fasfadsfdsafdsf"
private const val ENCODED_CHANGED_PASSWORD = "adsfasdfds"
private const val NAME = "Jon Doe"
private const val USER_ID = 100
private const val RESET_PASSWORD_CODE = "123456"

internal class UserServiceTest {

    private val userRepository: UserRepository = mockk()
    private val mailService: MailService = mockk()
    private val verificationTokenService: VerificationTokenService = mockk()
    private val resetPasswordTokenService: ResetPasswordTokenService = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()

    private val userService: UserService = UserService(
        userRepository,
        mailService,
        verificationTokenService,
        resetPasswordTokenService,
        passwordEncoder
    )

    @AfterEach
    fun cleanup() {
        unmockkAll()
        clearAllMocks()
    }

    private fun mockUser(): User {
        val user = User()
        user.id = USER_ID
        user.email = EMAIL
        user.password = NOT_ENCODED_PASSWORD
        user.isActive = false
        user.isBlocked = false
        user.name = NAME
        return user
    }

    private fun mockCorrectToken(user: User): VerificationToken {
        val toSave = VerificationToken()
        toSave.id = TOKEN_ID
        toSave.code = VALID_CODE_STRING
        toSave.user = user
        toSave.expiryDate = VALID_EXPIRATION_DATE
        return toSave
    }

    @Test
    fun userExists_TrueCaseTest() {
        // given
        every { userRepository.existsByEmail(EMAIL) } returns true

        // when
        val actual = userService.userExists(EMAIL)

        // then
        assert(actual)
    }

    @Test
    fun userExists_FalseCaseTest() {
        // given
        every { userRepository.existsByEmail(EMAIL) } returns false

        // when
        val actual = userService.userExists(EMAIL)

        // then
        assert(!actual)
    }

    @Test
    fun createUser_UserExistsCaseTest() {
        // given
        val user = mockUser()
        val userDTO: UserDTO = mockk()
        every { userDTO.email } returns EMAIL
        every { userDTO.toEntity(ERole.ADMIN_ROLE) } returns user
        every { userRepository.existsByEmail(EMAIL) } returns true

        // then
        assertThrows<UserAlreadyExistsException> {
            userService.createUser(userDTO, ERole.USER_ROLE)
        }
    }

    @Test
    fun createUser_AdminRoleCaseTest() {
        // given
        val user = mockUser()
        val userDTO: UserDTO = mockk()
        every { userDTO.toEntity(ERole.ADMIN_ROLE) } returns user
        every { userDTO.email } returns EMAIL
        every { userRepository.existsByEmail(EMAIL) } returns false
        every { passwordEncoder.encode(NOT_ENCODED_PASSWORD) } returns ENCODED_PASSWORD
        every { userRepository.save(any()) } returns user

        // when
        val actual = userService.createUser(userDTO, ERole.ADMIN_ROLE)

        // then
        assertEquals(EMAIL, actual.email)
        assertEquals(ENCODED_PASSWORD, actual.password)
        assertEquals(NAME, actual.name)
    }

    @Test
    fun createUser_UserRoleCaseTest() {
        // given
        val user = mockUser()
        val userDTO: UserDTO = mockk()
        every { userDTO.toEntity(ERole.USER_ROLE) } returns user
        every { userDTO.email } returns EMAIL
        every { userRepository.existsByEmail(EMAIL) } returns false
        every { passwordEncoder.encode(NOT_ENCODED_PASSWORD) } returns ENCODED_PASSWORD
        every { userRepository.save(any()) } returns user

        // when
        val actual = userService.createUser(userDTO, ERole.USER_ROLE)

        // then
        assertEquals(EMAIL, actual.email)
        assertEquals(ENCODED_PASSWORD, actual.password)
        assertEquals(NAME, actual.name)
    }

    @Test
    fun registerUser_SuccessCaseTest() {
        // given
        val user = mockUser()
        val token = VerificationToken()
        val userDTO: UserDTO = mockk()
        every { userDTO.toEntity() } returns user
        every { userDTO.email } returns EMAIL
        every { userRepository.existsByEmail(EMAIL) } returns false
        every { passwordEncoder.encode(NOT_ENCODED_PASSWORD) } returns ENCODED_PASSWORD
        every { userRepository.save(any()) } returns user
        every { verificationTokenService.createVerificationToken(user) } returns token
        every { mailService.sendUserVerificationMail(user, token.code) } returns Unit

        // when
        val actual = userService.registerUser(userDTO)

        // then
        assertEquals(ERole.USER_ROLE, user.role)
        assertEquals(EMAIL, actual.email)
        assertEquals(ENCODED_PASSWORD, actual.password)
        assertEquals(NAME, actual.name)
    }

    @Test
    fun registerUser_UserExistsCaseTest() {
        // given
        val user = mockUser()
        val userDTO: UserDTO = mockk()
        every { userDTO.email } returns EMAIL
        every { userDTO.toEntity(ERole.ADMIN_ROLE) } returns user
        every { userRepository.existsByEmail(EMAIL) } returns true

        // then
        assertThrows<UserAlreadyExistsException> {
            userService.registerUser(userDTO)
        }
    }

    @Test
    fun verifyUser_UserInactiveCaseTest() {
        // given
        val user = mockUser()
        val token = mockCorrectToken(user)
        every { verificationTokenService.findActiveByCode(VALID_CODE_STRING) } returns token
        every { userRepository.save(user) } returns user

        // when
        val actual = userService.verifyUser(VALID_CODE_STRING)

        // then
        assert(actual)
    }

    @Test
    fun verifyUser_UserActiveCaseTest() {
        // given
        val user = mockUser()
        user.isActive = true
        val token = mockCorrectToken(user)
        every { verificationTokenService.findActiveByCode(VALID_CODE_STRING) } returns token
        every { userRepository.save(user) } returns user

        // when
        val actual = userService.verifyUser(VALID_CODE_STRING)

        // then
        assert(!actual)
    }

    @Test
    fun toggleBlockedById_UserNotExistsCaseTest() {
        // given
        every { userRepository.findUserById(USER_ID) } throws UserNotFoundException()

        // then
        assertThrows<UserNotFoundException> {
            userService.toggleBlockedById(USER_ID)
        }
    }

    @Test
    fun toggleBlockedById_FalseCaseTest() {
        // given
        val user = mockUser()
        every { userRepository.findUserById(USER_ID) } returns user
        every { userRepository.save(user) } returns user

        // when
        val actual = userService.toggleBlockedById(USER_ID)

        // then
        assert(actual.isBlocked)
    }

    @Test
    fun toggleBlockedById_TrueCaseTest() {
        // given
        val user = mockUser()
        user.isBlocked = true
        every { userRepository.findUserById(USER_ID) } returns user
        every { userRepository.save(user) } returns user

        // when
        val actual = userService.toggleBlockedById(USER_ID)

        // then
        assert(!actual.isBlocked)
    }

    @Test
    fun changeUserRole_NotExistsCaseTest() {
        // given
        every { userRepository.findUserById(USER_ID) } returns null

        // then
        assertThrows<UserNotFoundException> {
            userService.changeUserRole(USER_ID, ERole.ADMIN_ROLE)
        }
    }

    @Test
    fun changeUserRole_UserRoleCaseTest() {
        // given
        val user = mockUser()
        every { userRepository.findUserById(USER_ID) } returns user
        every { userRepository.save(user) } returns user

        // when
        val actual = userService.changeUserRole(USER_ID, ERole.USER_ROLE)

        // then
        assertEquals(ERole.USER_ROLE, actual.role)
    }

    @Test
    fun changeUserRole_AdminRoleCaseTest() {
        // given
        val user = mockUser()
        every { userRepository.findUserById(USER_ID) } returns user
        every { userRepository.save(user) } returns user

        // when
        val actual = userService.changeUserRole(USER_ID, ERole.ADMIN_ROLE)

        // then
        assertEquals(ERole.ADMIN_ROLE, actual.role)
    }

    @Test
    fun updateActiveUserPassword_NotExistsCaseTest() {
        // given
        val authentication: Authentication = mockk()
        mockkStatic(SecurityContextHolder::class)
        every { SecurityContextHolder.getContext().authentication } returns authentication
        every { (authentication.principal as UserDetailsImpl).getId() } returns USER_ID
        every { userRepository.findUserById(USER_ID) } returns null

        // then
        assertThrows<UserNotFoundException> {
            userService.updateActiveUserPassword(PasswordDTO(NOT_ENCODED_CHANGED_PASSWORD))
        }
    }

    @Test
    fun updateActiveUserPassword_SuccessCaseTest() {
        // given
        val user = mockUser()
        val authentication: Authentication = mockk()
        mockkStatic(SecurityContextHolder::class)
        every { SecurityContextHolder.getContext().authentication } returns authentication
        every { (authentication.principal as UserDetailsImpl).getId() } returns USER_ID
        every { userRepository.findUserById(USER_ID) } returns user
        every { passwordEncoder.encode(NOT_ENCODED_CHANGED_PASSWORD) } returns ENCODED_CHANGED_PASSWORD
        every { userRepository.save(user) } returns user

        // when
        val actual = userService.updateActiveUserPassword(PasswordDTO(NOT_ENCODED_CHANGED_PASSWORD))

        // then
        assertEquals(ENCODED_CHANGED_PASSWORD, actual.password)
    }

    @Test
    fun getAllUserInfo() {
        // given
        val user = mockUser()
        every { userRepository.findAll() } returns listOf(user)

        // when
        val actual = userService.getAllUserInfo()

        // then
        assertEquals(1, actual.size)
        assertEquals(USER_ID, actual[0].id)
        assertEquals(EMAIL, actual[0].email)
        assertEquals(NAME, actual[0].name)
        assertEquals(ERole.USER_ROLE, actual[0].role)
        assertEquals(false, actual[0].isBlocked)
        assertEquals(false, actual[0].isActive)

    }

    @Test
    fun getUserInfoById_NotExistsCaseTest() {
        // given
        every { userRepository.findUserById(USER_ID) } returns null

        // then
        assertThrows<UserNotFoundException> {
            userService.getUserInfoById(USER_ID)
        }
    }

    @Test
    fun getUserInfoById_SuccessCaseTest() {
        // given
        val user = mockUser()
        every { userRepository.findUserById(USER_ID) } returns user

        // when
        val actual = userService.getUserInfoById(USER_ID)

        // then
        assertEquals(USER_ID, actual.id)
        assertEquals(EMAIL, actual.email)
        assertEquals(NAME, actual.name)
        assertEquals(ERole.USER_ROLE, actual.role)
        assertEquals(false, actual.isBlocked)
        assertEquals(false, actual.isActive)
    }

    @Test
    fun getActiveUserInfo_BadAuthenticationCaseTest() {
        // given
        val authentication: Authentication = mockk()
        mockkStatic(SecurityContextHolder::class)
        every { SecurityContextHolder.getContext().authentication } returns authentication
        every { authentication.principal is UserDetailsImpl } returns false

        // then
        assertThrows<UserNotFoundException> {
            userService.getActiveUserInfo()
        }
    }

    @Test
    fun getActiveUserInfo_NotExistsCaseTest() {
        // given
        val authentication: Authentication = mockk()
        mockkStatic(SecurityContextHolder::class)
        every { SecurityContextHolder.getContext().authentication } returns authentication
        every { (authentication.principal as UserDetailsImpl).getId() } returns USER_ID
        every { userRepository.findUserById(USER_ID) } returns null

        // then
        assertThrows<UserNotFoundException> {
            userService.getActiveUserInfo()
        }
    }

    @Test
    fun getActiveUserInfo_SuccessCaseTest() {
        // given
        val user = mockUser()
        val authentication: Authentication = mockk()
        mockkStatic(SecurityContextHolder::class)
        every { SecurityContextHolder.getContext().authentication } returns authentication
        every { (authentication.principal as UserDetailsImpl).getId() } returns USER_ID
        every { userRepository.findUserById(USER_ID) } returns user

        // when
        val actual = userService.getActiveUserInfo()

        // then
        assertEquals(USER_ID, actual.id)
        assertEquals(EMAIL, actual.email)
        assertEquals(NAME, actual.name)
        assertEquals(ERole.USER_ROLE, actual.role)
        assertEquals(false, actual.isBlocked)
        assertEquals(false, actual.isActive)
    }

    @Test
    fun resetPassword_UserNotExistsCaseTest() {
        // given
        every { userRepository.findUserByEmail(EMAIL) } returns null

        // then
        assertThrows<UserNotFoundException> {
            userService.resetPassword(EMAIL, RESET_PASSWORD_CODE, PasswordDTO(NOT_ENCODED_CHANGED_PASSWORD))
        }
    }

    @Test
    fun resetPassword_CodeIncorrectCaseTest() {
        // given
        val user = mockUser()
        every { userRepository.findUserByEmail(EMAIL) } returns user
        every { resetPasswordTokenService.isCodeCorrect(USER_ID, RESET_PASSWORD_CODE) } returns false

        // then
        assertThrows<TokenCodeIncorrectException> {
            userService.resetPassword(EMAIL, RESET_PASSWORD_CODE, PasswordDTO(NOT_ENCODED_CHANGED_PASSWORD))
        }
    }

    @Test
    fun resetPassword_CodeCorrectCaseTest() {
        // given
        val user = mockUser()
        every { userRepository.findUserByEmail(EMAIL) } returns user
        every { userRepository.findUserById(USER_ID) } returns user
        every { resetPasswordTokenService.isCodeCorrect(USER_ID, RESET_PASSWORD_CODE) } returns true
        every { resetPasswordTokenService.deactivateUserTokens(USER_ID) } returns emptyList()
        every { passwordEncoder.encode(NOT_ENCODED_CHANGED_PASSWORD) } returns ENCODED_CHANGED_PASSWORD
        every { userRepository.save(user) } returns user

        // given
        val actual = userService.resetPassword(EMAIL, RESET_PASSWORD_CODE, PasswordDTO(NOT_ENCODED_CHANGED_PASSWORD))

        // then
        assertEquals(ENCODED_CHANGED_PASSWORD, actual.password)
    }
}