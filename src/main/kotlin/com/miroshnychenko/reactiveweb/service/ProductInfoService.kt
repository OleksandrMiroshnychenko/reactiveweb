package com.miroshnychenko.reactiveweb.service

import com.miroshnychenko.reactiveweb.dto.Product
import com.miroshnychenko.reactiveweb.util.LoggingUtil.logOnError
import com.miroshnychenko.reactiveweb.util.LoggingUtil.logOnNext
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux

@Service
class ProductInfoService(private val productInfoServiceWebClient: WebClient, private val log: Logger) {

    fun getOrderProducts(productCode: String): Flux<Product> =
        productInfoServiceWebClient.get().uri(
            UriComponentsBuilder.fromUriString("/productInfoService/product/names")
                .queryParam("productCode", productCode)
                .buildAndExpand()
                .toUriString()
        )
            .retrieve()
            .bodyToFlux(Product::class.java)
            .doOnEach(logOnError { log.info("Exception from ProductInfoService: ${it?.message}") })
            .doOnEach(logOnNext { log.info("Found a product: $it") })
            .onErrorResume { Flux.empty() }
}