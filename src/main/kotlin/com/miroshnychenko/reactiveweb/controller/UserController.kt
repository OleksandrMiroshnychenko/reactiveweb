package com.miroshnychenko.reactiveweb.controller

import com.miroshnychenko.reactiveweb.dto.OrderInfo
import com.miroshnychenko.reactiveweb.dto.User
import com.miroshnychenko.reactiveweb.manager.OrderInfoManager
import com.miroshnychenko.reactiveweb.repository.UserRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/user")
class UserController(private val orderInfoManager: OrderInfoManager, private val userRepository: UserRepository) {

    @GetMapping(value = ["/user"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun getUserById(@RequestParam userId: String): Mono<User> {
        return userRepository.findById(userId)
    }

    @GetMapping(value = ["/orderInfosV2"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun getOrderInfosByUserId(@RequestParam userId: String): Flux<OrderInfo> {
        return orderInfoManager.getBestOrderInfosByUserId(userId)
    }
}