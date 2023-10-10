package edu.pwr.iotmobile.repositories

import edu.pwr.iotmobile.entities.User
import jakarta.validation.constraints.Email
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findUserById(id: Int) : User?
    fun findUserByEmail(email: String) : User?
    fun existsByEmail(email: String) : Boolean
}