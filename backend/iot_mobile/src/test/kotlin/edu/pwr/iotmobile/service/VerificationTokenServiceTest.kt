package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.entities.VerificationToken
import edu.pwr.iotmobile.error.exception.TokenNotFoundException
import edu.pwr.iotmobile.repositories.VerificationTokenRepository
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
private const val TOKEN_ID = 100

internal class VerificationTokenServiceTest {


    private val verificationTokenRepository: VerificationTokenRepository = mockk()
    private val verificationTokenService: VerificationTokenService = VerificationTokenService(verificationTokenRepository)

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

    private fun mockCorrectToken(user: User = mockUser()) : VerificationToken {
        val toSave = VerificationToken()
        toSave.id = TOKEN_ID
        toSave.code = VALID_CODE_STRING
        toSave.user = user
        toSave.expiryDate = VALID_EXPIRATION_DATE
        return toSave
    }

    private fun mockExpiredToken(user: User = mockUser()) : VerificationToken {
        val toSave = VerificationToken()
        toSave.id = TOKEN_ID
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
        every { verificationTokenRepository.save(any())} returns saved

        // when
        val token = verificationTokenService.createVerificationToken(user)

        // then
        assertEquals(TOKEN_ID, token.id)
        assertEquals(VALID_USER_ID, token.user.id)
        assertEquals(VALID_CODE_STRING, token.code)
        assertEquals(VALID_EXPIRATION_DATE, token.expiryDate)
    }

    @Test
    fun findActiveByCode_CorrectCaseTest() {
        //given
        val expected = mockCorrectToken()
        every { verificationTokenRepository.findByCode(VALID_CODE_STRING)} returns expected

        // when
        val actual = verificationTokenService.findActiveByCode(VALID_CODE_STRING)

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun findActiveByCode_IncorrectCodeCaseTest() {
        // given
        every { verificationTokenRepository.findByCode(INVALID_CODE_STRING)} returns null

        // then
        assertThrows<TokenNotFoundException> {
            verificationTokenService.findActiveByCode(INVALID_CODE_STRING)
        }
    }

    @Test
    fun findActiveByCode_ExpiredCaseTest() {
        // given
        val expiredToken = mockExpiredToken()
        every { verificationTokenRepository.findByCode(VALID_CODE_STRING)} returns expiredToken

        // then
        assertThrows<TokenNotFoundException> {
            verificationTokenService.findActiveByCode(VALID_CODE_STRING)
        }
    }
}