package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.entities.ResetPasswordToken
import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.repositories.ResetPasswordTokenRepository
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.random.Random

private const val EXPIRATION = 1L
private val INVALID_EXPIRATION_DATE = Date.from(Instant.parse("2000-01-01T00:00:00.00Z"))
private val VALID_EXPIRATION_DATE = Date.from(Instant.now().plus(EXPIRATION, ChronoUnit.HOURS))
private const val VALID_CODE_STRING = "111111"
private const val INVALID_CODE_STRING = "000000"
private const val VALID_USER_ID = 1
private const val INVALID_USER_ID = 2
private const val TOKEN_ID = 100

internal class ResetPasswordTokenServiceTest {

    private val resetPasswordTokenRepository: ResetPasswordTokenRepository = mockk()
    private val resetPasswordTokenService: ResetPasswordTokenService = ResetPasswordTokenService(resetPasswordTokenRepository)

    @AfterEach
    fun cleanup() {
        unmockkAll()
        clearAllMocks()
    }

    private fun mockUser() : User {
        val user = User()
        user.id = VALID_USER_ID
        return user
    }

    private fun mockCorrectToken(user: User = mockUser()) : ResetPasswordToken {
        val toSave = ResetPasswordToken()
        toSave.id = TOKEN_ID
        toSave.active = true
        toSave.code = VALID_CODE_STRING
        toSave.user = user
        toSave.expiryDate = VALID_EXPIRATION_DATE
        return toSave
    }

    private fun mockExpiredToken(user: User = mockUser()) : ResetPasswordToken {
        val toSave = ResetPasswordToken()
        toSave.id = TOKEN_ID
        toSave.active = true
        toSave.code = VALID_CODE_STRING
        toSave.user = user
        toSave.expiryDate = INVALID_EXPIRATION_DATE
        return toSave
    }

    @Test
    fun createVerificationTokenTest() {
        // given
        mockkStatic(Date::class)
        every { Date.from(any()) } returns VALID_EXPIRATION_DATE
        mockkObject(Random)
        every { Random.nextInt(10) } returns 1
        val user = mockUser()
        val saved = mockCorrectToken(user)
        every { resetPasswordTokenRepository.save(any())} returns saved

        // when
        val token = resetPasswordTokenService.createVerificationToken(user)

        // then
        assertEquals(TOKEN_ID, token.id)
        assertEquals(VALID_USER_ID, token.user.id)
        assertEquals(true, token.active)
        assertEquals(VALID_CODE_STRING, token.code)
        assertEquals(VALID_EXPIRATION_DATE, token.expiryDate)
    }

    @Test
    fun isCodeCorrect_CorrectCaseTest() {
        //given
        val correctToken = mockCorrectToken()
        every { resetPasswordTokenRepository.findByCode(VALID_CODE_STRING)} returns correctToken

        // when
        val actual = resetPasswordTokenService.isCodeCorrect(VALID_USER_ID, VALID_CODE_STRING)

        // then
        assert(actual)
    }

    @Test
    fun isCodeCorrect_ExpiredCaseTest() {
        //given
        val correctToken = mockExpiredToken()
        every { resetPasswordTokenRepository.findByCode(VALID_CODE_STRING)} returns correctToken

        // when
        val actual = resetPasswordTokenService.isCodeCorrect(VALID_USER_ID, VALID_CODE_STRING)

        // then
        assert(!actual)
    }

    @Test
    fun isCodeCorrect_IncorrectCodeCaseTest() {
        //given
        every { resetPasswordTokenRepository.findByCode(INVALID_CODE_STRING)} returns null

        // when
        val actual = resetPasswordTokenService.isCodeCorrect(VALID_USER_ID, INVALID_CODE_STRING)

        // then
        assert(!actual)
    }

    @Test
    fun isCodeCorrect_IncorrectUserCaseTest() {
        //given
        val correctToken = mockCorrectToken()
        every { resetPasswordTokenRepository.findByCode(VALID_CODE_STRING)} returns correctToken

        // when
        val actual = resetPasswordTokenService.isCodeCorrect(INVALID_USER_ID, VALID_CODE_STRING)

        // then
        assert(!actual)
    }


    @Test
    fun deactivateUserTokensTest() {
        // given
        val tokenList = listOf(mockCorrectToken())
        every { resetPasswordTokenRepository.findAllByUserId(VALID_USER_ID)} returns tokenList
        every { resetPasswordTokenRepository.saveAll(tokenList) } returns tokenList

        // when
        val resultTokenList = resetPasswordTokenService.deactivateUserTokens(VALID_USER_ID)

        // then
        val allInactive = resultTokenList.all { !it.active }
        assert(allInactive)
    }

}