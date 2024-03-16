package com.miroshnychenko.reactiveweb.service

import com.miroshnychenko.reactiveweb.dto.Product
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToFlow
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.concurrent.CompletableFuture

@Service
class ProductInfoService(private val webClient: WebClient) {
    fun getOrderProducts(productCode: String): Flux<Product> =
        webClient.get().uri(
            UriComponentsBuilder.fromUriString("http://localhost:8083/productInfoService/product/names")
                .queryParam("productCode", productCode)
                .buildAndExpand()
                .toUriString()
        )
            .retrieve()
            .bodyToFlux(Product::class.java)
            .onErrorResume { Flux.empty() }
}