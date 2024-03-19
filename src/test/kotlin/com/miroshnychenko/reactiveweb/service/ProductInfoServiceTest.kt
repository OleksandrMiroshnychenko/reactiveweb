package com.miroshnychenko.reactiveweb.service

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

@WireMockTest(httpPort = 8079)
class ProductInfoServiceTest {

    private val webClient = WebClient.builder().baseUrl("http://localhost:8079").build()

    private val productInfoService = ProductInfoService(webClient, LoggerFactory.getLogger(""))

    @Test
    fun getOrdersByPhone() {

        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/productInfoService/product/names"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"productId\": \"123\", \"productName\": \"321\", \"productCode\": \"231\", \"score\": \"10.0\"}")
                )
        )

        val ordersFlux = productInfoService.getOrderProducts("")

        StepVerifier.create(ordersFlux).assertNext {
            Assertions.assertEquals("123", it.productId)
            Assertions.assertEquals("321", it.productName)
            Assertions.assertEquals("231", it.productCode)
            Assertions.assertEquals(10.0, it.score)
        }.verifyComplete()
    }

    @Test
    fun getOrdersByPhoneWhenProductInfoServiceIsDown() {

        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/productInfoService/product/names"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(404)
                )
        )

        val ordersFlux = productInfoService.getOrderProducts("")

        StepVerifier.create(ordersFlux).verifyComplete()
    }
}