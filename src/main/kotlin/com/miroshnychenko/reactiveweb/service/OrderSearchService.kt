package com.miroshnychenko.reactiveweb.service

import com.miroshnychenko.reactiveweb.dto.Order
import com.miroshnychenko.reactiveweb.util.LoggingUtil.logOnNext
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder.fromUriString
import reactor.core.publisher.Flux
import reactor.util.context.Context

@Service
class OrderSearchService(private val webClient: WebClient, private val log: Logger) {
    fun getUserOrders(phoneNumber: String): Flux<Order> = webClient.get().uri(
        fromUriString("http://localhost:8082/orderSearchService/order/phone")
            .queryParam("phoneNumber", phoneNumber)
            .buildAndExpand()
            .toUriString()
    )
        .retrieve()
        .bodyToFlux(Order::class.java)
        .doOnEach(logOnNext { log.info("Found an order $it") })
}