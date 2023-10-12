package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.entities.ResetPasswordToken
import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.error.exception.TokenNotFoundException
import edu.pwr.iotmobile.repositories.ResetPasswordTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

private const val EXPIRATION = 1L
private const val TOKEN_SIZE = 6


@Service
class ResetPasswordTokenService(val resetPasswordTokenRepository: ResetPasswordTokenRepository) {

    private fun generateRandomCode() : String {
        return (1..TOKEN_SIZE).map { kotlin.random.Random.nextInt(10) }.joinToString("")
    }

    private fun calculateExpiryDate() : Date {
        return Date.from(Instant.now().plus(EXPIRATION, ChronoUnit.HOURS))
    }

    fun createVerificationToken(user: User) : ResetPasswordToken {
        val token = ResetPasswordToken(generateRandomCode(), user, calculateExpiryDate(), true)
        return resetPasswordTokenRepository.save(token)
    }

    fun isCodeCorrect(userId: Int, code: String) : Boolean {
        return try {
            val resetPasswordToken = findByCode(code)
            isTokenActive(resetPasswordToken) and (resetPasswordToken.user.id == userId)
        } catch (e: TokenNotFoundException) {
            false
        }
    }

    private fun findByCode(token: String): ResetPasswordToken {
        return resetPasswordTokenRepository.findByCode(token) ?: throw TokenNotFoundException()
    }

    @Transactional
    fun deactivateUserTokens(userId: Int) : List<ResetPasswordToken> {
        val userResetTokenList = resetPasswordTokenRepository.findAllByUserId(userId)
        userResetTokenList.forEach {
            it.active = false
        }
        return resetPasswordTokenRepository.saveAll(userResetTokenList)
    }

    private fun isTokenActive(token: ResetPasswordToken) : Boolean {
        return token.expiryDate.after(Date.from(Instant.now())) and token.active
    }
}