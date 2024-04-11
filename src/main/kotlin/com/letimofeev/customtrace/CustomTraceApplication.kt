package com.letimofeev.customtrace

import com.letimofeev.customtrace.config.TracingConfig.Companion.TRACE_KEY
import io.micrometer.context.ContextRegistry
import org.slf4j.MDC
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks

@SpringBootApplication
class CustomTraceApplication

fun main(args: Array<String>) {
    Hooks.enableAutomaticContextPropagation()

    ContextRegistry.getInstance()
        .registerThreadLocalAccessor(
            TRACE_KEY,
            { MDC.get(TRACE_KEY) },
            { myTrace -> MDC.put(TRACE_KEY, myTrace) },
            { MDC.remove(TRACE_KEY) }
        )

    runApplication<CustomTraceApplication>(*args)
}
