package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.entities.ResetPasswordToken
import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.error.exception.TokenNotFoundException
import edu.pwr.iotmobile.repositories.ResetPasswordTokenRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

private val EXPIRATION = 1L

@Service
class ResetPasswordTokenService(val resetPasswordTokenRepository: ResetPasswordTokenRepository) {

    private fun generateRandomCode() : String {
        val random = Random()
        return (1..6).map { random.nextInt(10) }.joinToString("")
    }

    private fun calculateExpiryDate() : Date {
        return Date.from(Instant.now().plus(EXPIRATION, ChronoUnit.HOURS))
    }

    fun createVerificationToken(user: User) : ResetPasswordToken {
        val token = ResetPasswordToken(generateRandomCode(), user, calculateExpiryDate(), true)
        return resetPasswordTokenRepository.save(token)
    }

    fun isCodeCorrect(userId: Int, code: String) : Boolean {
        val resetPasswordToken = findByCode(code)
        return isTokenActive(resetPasswordToken) and (resetPasswordToken.user.id == userId)
    }

    private fun findByCode(token: String): ResetPasswordToken {
        return resetPasswordTokenRepository.findByCode(token) ?: throw TokenNotFoundException()
    }

    fun deactivateUserTokens(userId: Int) {
        val userResetTokenList = resetPasswordTokenRepository.findAllByUserId(userId)
        userResetTokenList.forEach {
            it.active = false
        }
        resetPasswordTokenRepository.saveAll(userResetTokenList)
    }

    private fun isTokenActive(token: ResetPasswordToken) : Boolean {
        return token.expiryDate.after(Date.from(Instant.now())) and token.active
    }
}