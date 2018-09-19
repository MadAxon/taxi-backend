package me.flashka.web.taxi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TaxiApplication

fun main(args: Array<String>) {
    runApplication<TaxiApplication>(*args)
}
