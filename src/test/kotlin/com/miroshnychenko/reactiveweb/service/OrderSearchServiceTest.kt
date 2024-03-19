package com.miroshnychenko.reactiveweb.service

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

@WireMockTest(httpPort = 8079)
class OrderSearchServiceTest {

    private val webClient = WebClient.builder().baseUrl("http://localhost:8079").build()

    private val orderSearchService = OrderSearchService(webClient, LoggerFactory.getLogger(""))

    @Test
    fun getOrdersByPhone() {

        stubFor(get(urlPathEqualTo("/orderSearchService/order/phone"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"phoneNumber\": \"123\", \"orderNumber\": \"124\", \"productCode\": \"125\"}")))

        val ordersFlux = orderSearchService.getUserOrders("")

        StepVerifier.create(ordersFlux).assertNext {
            assertEquals("123", it.phoneNumber)
            assertEquals("124", it.orderNumber)
            assertEquals("125", it.productCode)
        }.verifyComplete()
    }
}