package com.miroshnychenko.reactiveweb.util

import org.slf4j.MDC
import reactor.core.publisher.Signal
import java.util.*
import java.util.function.Consumer


object LoggingUtil {

    fun <T> logOnNext(logStatement: Consumer<T>): Consumer<Signal<T>> = Consumer<Signal<T>> { signal ->
        if (!signal.isOnNext) return@Consumer
        val toPutInMdc: Optional<String> = signal.contextView.getOrEmpty("CONTEXT_KEY")
        toPutInMdc.ifPresentOrElse({ tpim ->
            MDC.putCloseable("MDC_KEY", tpim).use {
                logStatement.accept(signal.get()!!)
            }
        },
            { logStatement.accept(signal.get()!!) })
    }

    fun logOnError(errorLogStatement: Consumer<Throwable?>): Consumer<Signal<*>> {
        return Consumer { signal: Signal<*> ->
            if (!signal.isOnError) return@Consumer
            val toPutInMdc: Optional<String> = signal.contextView.getOrEmpty("CONTEXT_KEY")
            toPutInMdc.ifPresentOrElse({ tpim ->
                MDC.putCloseable("MDC_KEY", tpim).use {
                    errorLogStatement.accept(signal.throwable)
                }
            },
                { errorLogStatement.accept(signal.throwable) })
        }
    }
}