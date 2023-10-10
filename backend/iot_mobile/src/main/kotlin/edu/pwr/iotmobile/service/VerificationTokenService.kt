package edu.pwr.iotmobile.service

import edu.pwr.iotmobile.entities.User
import edu.pwr.iotmobile.entities.VerificationToken
import edu.pwr.iotmobile.error.exception.TokenNotFoundException
import edu.pwr.iotmobile.repositories.VerificationTokenRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

private val EXPIRATION = 24L

@Service
class VerificationTokenService(val verificationTokenRepository: VerificationTokenRepository) {

    private fun generateRandomCode() : String {
        val random = Random()
        return (1..6).map { random.nextInt(10) }.joinToString("")
    }

    private fun calculateExpiryDate() : Date {
        return Date.from(Instant.now().plus(EXPIRATION, ChronoUnit.HOURS))
    }

    fun createVerificationToken(user: User) : VerificationToken {
        val token = VerificationToken(generateRandomCode(), user, calculateExpiryDate())
        return verificationTokenRepository.save(token)
    }

    fun findActiveByCode(token: String) : VerificationToken {
        val verificationToken = verificationTokenRepository.findByCode(token) ?: throw TokenNotFoundException()

        return if (isTokenActive(verificationToken)) verificationToken
        else throw TokenNotFoundException()
    }

    private fun isTokenActive(token: VerificationToken) : Boolean {
        return token.expiryDate.after(Date.from(Instant.now()))
    }
}