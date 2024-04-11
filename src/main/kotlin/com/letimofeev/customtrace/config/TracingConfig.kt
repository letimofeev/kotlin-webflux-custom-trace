package com.letimofeev.customtrace.config

import brave.baggage.BaggageField
import brave.baggage.BaggagePropagation
import brave.baggage.BaggagePropagationConfig
import brave.propagation.Propagation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.WebFilter
import reactor.util.context.Context

@Configuration
class TracingConfig {
    @Bean
    fun propagationFactory(factory: BaggagePropagation.FactoryBuilder): Propagation.Factory =
        factory.add(
            BaggagePropagationConfig.SingleBaggageField.remote(
                BaggageField.create(TRACE_KEY)
            )
        ).build()

    @Bean
    fun customTraceFilter(): WebFilter =
        WebFilter { exchange, chain ->
            val myTrace = exchange.request.headers[TRACE_KEY]

            if (!myTrace.isNullOrEmpty()) {
                chain.filter(exchange)
                    .contextWrite(Context.of(mapOf(TRACE_KEY to myTrace.first())))
            } else {
                chain.filter(exchange)
            }
        }

    companion object {
        const val TRACE_KEY = "mytrace"
    }
}
