package me.flashka.web.taxi.service

import me.flashka.web.taxi.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailServiceImpl(
        private val userRepository: UserRepository
) : UserDetailsService {

    @Transactional(readOnly = true)
    override fun loadUserByUsername(p0: String?): UserDetails? {
        val user = userRepository.findByPhoneNumber(p0)
        if (user != null) {
            val grantedAuthorities: HashSet<GrantedAuthority> = HashSet()
            grantedAuthorities.add(SimpleGrantedAuthority(user.role.name))

            return User(user.phoneNumber, user.password, grantedAuthorities)
        }
        return null
    }

}