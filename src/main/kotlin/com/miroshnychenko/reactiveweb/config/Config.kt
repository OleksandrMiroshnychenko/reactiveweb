package com.miroshnychenko.reactiveweb.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import io.netty.channel.ChannelOption
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.RequestParameterBuilder
import springfox.documentation.schema.ScalarType
import springfox.documentation.service.ParameterType
import springfox.documentation.service.RequestParameter
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import java.time.Duration

@Configuration
@EnableReactiveMongoRepositories(basePackages = ["com.miroshnychenko.reactiveweb.repository"])
class Config : AbstractReactiveMongoConfiguration() {

    override fun getDatabaseName(): String {
        return "admin"
    }

    override fun reactiveMongoClient(): MongoClient {
        val connectionString = ConnectionString("mongodb://root:pwd@localhost:27017/admin")
        val mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build()

        return MongoClients.create(mongoClientSettings)
    }

    public override fun getMappingBasePackages(): MutableCollection<String> {
        return mutableSetOf("com.miroshnychenko.reactiveweb.dto")
    }

    @Bean
    fun usersMicroserviceOpenAPI(): OpenAPI {
        return OpenAPI().info(
            Info().title("MIROSHNYCHENKO").description("reactiveweb").version("0.0.1-SNAPSHOT")
        )
    }

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .globalRequestParameters(globalRequestParameters())
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController::class.java))
            .build()
    }

    fun globalRequestParameters(): List<RequestParameter> = listOf(
        RequestParameterBuilder()
            .`in`(ParameterType.HEADER)
            .name("requestId")
            .required(true)
            .query { param -> param.model { model -> model.scalarModel(ScalarType.STRING) } }.build()
    )

    @Bean
    fun httpClient(): HttpClient = HttpClient.create()
//        .responseTimeout(Duration.ofSeconds(5))
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

    @Bean
    fun webClient(httpClient: HttpClient): WebClient =
        WebClient.builder().clientConnector(ReactorClientHttpConnector(httpClient)).build()
}