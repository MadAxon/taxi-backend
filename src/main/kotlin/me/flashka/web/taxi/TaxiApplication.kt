package me.flashka.web.taxi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TaxiApplication: SpringBootServletInitializer()

fun main(args: Array<String>) {
    runApplication<TaxiApplication>(*args)
}
