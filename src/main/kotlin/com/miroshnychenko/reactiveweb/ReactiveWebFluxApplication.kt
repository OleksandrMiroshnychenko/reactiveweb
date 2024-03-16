package com.miroshnychenko.reactiveweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveWebFluxApplication

fun main(args: Array<String>) {
	runApplication<ReactiveWebFluxApplication>(*args)
}
