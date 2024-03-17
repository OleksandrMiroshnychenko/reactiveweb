package com.miroshnychenko.reactiveweb.controller

import com.miroshnychenko.reactiveweb.dto.OrderInfo
import com.miroshnychenko.reactiveweb.manager.OrderInfoManager
import com.miroshnychenko.reactiveweb.util.LoggingUtil.logOnNext
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.util.context.Context
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/user")
class UserController(private val orderInfoManager: OrderInfoManager) {

    @Autowired
    lateinit var log: Logger

    @GetMapping(value = ["/orderInfosV2"], produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun getOrderInfosByUserId(
        @RequestParam userId: String,
        @RequestHeader(required = true, name = "requestId") requestId: String
    ): Flux<OrderInfo> {
        log.info("Got a request $requestId for userId $userId")
        val bestOrderInfosByUserId = orderInfoManager.getBestOrderInfosByUserId(userId)
            .doOnEach(logOnNext { log.info("Found orderInfo $it") })
            .contextWrite(Context.of("CONTEXT_KEY", requestId));
        return bestOrderInfosByUserId
    }
}