package com.letimofeev.customtrace.controller

import com.letimofeev.customtrace.config.TracingConfig.Companion.TRACE_KEY
import com.letimofeev.customtrace.domain.Subscription
import kotlinx.coroutines.reactor.awaitSingle
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@RestController
class SubscriptionController(webclientBuilder: WebClient.Builder) {
    private final val webClient: WebClient = webclientBuilder.baseUrl("http://localhost:8080/").build()

    @GetMapping("/subscription1")
    fun subscription1(): Mono<Subscription> {
        logger.info { "subscription1 request" }

        return webClient.get()
            .uri("/subscription2")
            .header(TRACE_KEY, "prikol")
            .retrieve()
            .bodyToMono(Subscription::class.java)
    }

    @GetMapping("/subscription2")
    suspend fun subscription2(): Subscription {
        logger.info { "subscription2 request" }

        return webClient.get()
            .uri("/subscription3")
            .retrieve()
            .bodyToMono(Subscription::class.java)
            .awaitSingle()
    }

    @GetMapping("/subscription3")
    suspend fun subscription3(@RequestHeader headers: Map<String, String>): Subscription {
        logger.info { "subscription3 request, headers: $headers" }

        return Subscription(description = "govno")
    }

    companion object : KLogging()
}