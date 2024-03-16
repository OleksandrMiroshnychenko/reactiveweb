package com.miroshnychenko.reactiveweb.controller

import com.miroshnychenko.reactiveweb.dto.OrderInfo
import com.miroshnychenko.reactiveweb.manager.OrderInfoManager
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/user")
class UserController(private val orderInfoManager: OrderInfoManager) {

    @GetMapping(value = ["/orderInfosV2"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun getOrderInfosByUserId(
        @RequestParam userId: String,
        @ApiIgnore exchange: ServerWebExchange
    ): Flux<OrderInfo> {
        val requestId = exchange.request.headers["requestId"]!![0]
        println(requestId)
        return orderInfoManager.getBestOrderInfosByUserId(userId)
    }
}