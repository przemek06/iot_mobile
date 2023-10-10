package edu.pwr.iotmobile.security

import edu.pwr.iotmobile.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = username?.let { userRepository.findUserByEmail(it) }
        return UserDetailsImpl(user ?: throw UsernameNotFoundException(username))
    }

}