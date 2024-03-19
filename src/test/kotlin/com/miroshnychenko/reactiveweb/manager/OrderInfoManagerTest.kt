package com.miroshnychenko.reactiveweb.manager

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.miroshnychenko.reactiveweb.dto.User
import com.miroshnychenko.reactiveweb.service.OrderSearchService
import com.miroshnychenko.reactiveweb.service.ProductInfoService
import com.miroshnychenko.reactiveweb.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@WireMockTest(httpPort = 8079)
class OrderInfoManagerTest {

    private val userService: UserService = mockk()

    private val webClient = WebClient.builder().baseUrl("http://localhost:8079").build()

    private val orderSearchService = OrderSearchService(webClient, LoggerFactory.getLogger(""))

    private val productInfoService = ProductInfoService(webClient, LoggerFactory.getLogger(""))

    private val orderInfoManager = OrderInfoManager(userService, orderSearchService, productInfoService)

    @Test
    fun getBestProductByProductCode() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/productInfoService/product/names"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                            "[{\"productId\": \"123\", \"productName\": \"321\", \"productCode\": \"231\", \"score\": \"10.0\"}," +
                                    "{\"productId\": \"124\", \"productName\": \"421\", \"productCode\": \"231\", \"score\": \"11.0\"}]"
                        )
                )
        )

        val productMono = orderInfoManager.getBestProductByProductCode("")

        StepVerifier.create(productMono).assertNext {
            Assertions.assertEquals("124", it.productId)
            Assertions.assertEquals("421", it.productName)
            Assertions.assertEquals("231", it.productCode)
            Assertions.assertEquals(11.0, it.score)
        }.verifyComplete()
    }

    @Test
    fun getBestOrderInfosByUserId() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/productInfoService/product/names"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                            "[{\"productId\": \"123\", \"productName\": \"321\", \"productCode\": \"231\", \"score\": \"10.0\"}," +
                                    "{\"productId\": \"124\", \"productName\": \"421\", \"productCode\": \"231\", \"score\": \"11.0\"}]"
                        )
                )
        )

        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/orderSearchService/order/phone"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"phoneNumber\": \"123\", \"orderNumber\": \"124\", \"productCode\": \"231\"}")
                )
        )

        val user = User("Petar", "123123123")
        user.id = "user1"
        every { userService.getUserPhoneById(any()) } returns Mono.just(user)

        val orderInfosFlux = orderInfoManager.getBestOrderInfosByUserId("")

        StepVerifier.create(orderInfosFlux).assertNext {
            Assertions.assertEquals("124", it.orderNumber)
            Assertions.assertEquals("231", it.productCode)
            Assertions.assertEquals("124", it.productId)
            Assertions.assertEquals("421", it.productName)
            Assertions.assertEquals("Petar", it.userName)
            Assertions.assertEquals("123123123", it.phoneNumber)
        }.verifyComplete()
    }
}