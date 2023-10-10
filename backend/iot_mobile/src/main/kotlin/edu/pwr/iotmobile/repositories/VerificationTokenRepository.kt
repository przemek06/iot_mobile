package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VerificationTokenRepository : JpaRepository<VerificationToken, Int> {
    fun findByCode(code: String) : VerificationToken?
}