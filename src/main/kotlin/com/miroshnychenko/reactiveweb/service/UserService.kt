package com.miroshnychenko.reactiveweb.service

import com.miroshnychenko.reactiveweb.dto.User
import com.miroshnychenko.reactiveweb.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(private val userRepository: UserRepository) {

    fun getUserPhoneById(id: String): Mono<User> = userRepository.findById(id)
}