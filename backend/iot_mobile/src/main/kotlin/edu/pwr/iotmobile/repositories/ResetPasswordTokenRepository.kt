package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.ResetPasswordToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ResetPasswordTokenRepository : JpaRepository<ResetPasswordToken, Int> {
    fun findByCode(token: String) : ResetPasswordToken?
    fun findAllByUserId(id: Int) : List<ResetPasswordToken>

}