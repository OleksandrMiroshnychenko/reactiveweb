package com.miroshnychenko.reactiveweb.service

import com.miroshnychenko.reactiveweb.dto.User
import com.miroshnychenko.reactiveweb.repository.UserRepository
import com.miroshnychenko.reactiveweb.util.LoggingUtil
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(private val userRepository: UserRepository, private val log: Logger) {

    fun getUserPhoneById(id: String): Mono<User> = userRepository.findById(id).doOnEach(LoggingUtil.logOnError {
        log.info(
            "Exception from UserService: ${it?.message}"
        )
    })
        .doOnEach(LoggingUtil.logOnNext { log.info("Found a user: $it") })
}