package com.miroshnychenko.reactiveweb.manager

import com.miroshnychenko.reactiveweb.dto.Order
import com.miroshnychenko.reactiveweb.dto.OrderInfo
import com.miroshnychenko.reactiveweb.dto.Product
import com.miroshnychenko.reactiveweb.dto.User
import com.miroshnychenko.reactiveweb.service.OrderSearchService
import com.miroshnychenko.reactiveweb.service.ProductInfoService
import com.miroshnychenko.reactiveweb.service.UserService
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class OrderInfoManager(
    private val userService: UserService,
    private val orderSearchService: OrderSearchService,
    private val productInfoService: ProductInfoService
) {

    fun getBestOrderInfosByUserId(userId: String): Flux<OrderInfo> = getUser(userId).flatMapMany { u ->
        getOrdersByPhone(u.phone).map { o ->
            OrderInfo(
                o.orderNumber,
                u.name,
                u.phone,
                o.productCode,
            )
        }.flatMap { oi ->
            getBestProductByProductCode(oi.productCode).map { p ->
                oi.productName = p.productName
                oi.productId = p.productId
                oi
            }
                .switchIfEmpty(Mono.just(oi))
        }
    }

    private fun getUser(userId: String): Mono<User> = userService.getUserPhoneById(userId)

    private fun getOrdersByPhone(phone: String): Flux<Order> = orderSearchService.getUserOrders(phone)

    fun getBestProductByProductCode(productCode: String): Mono<Product> =
        productInfoService.getOrderProducts(productCode)
            .reduce { o1, o2 -> if (o1.score > o2.score) o1 else o2 }
}