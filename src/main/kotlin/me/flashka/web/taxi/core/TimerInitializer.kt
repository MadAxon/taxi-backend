package me.flashka.web.taxi.core

import me.flashka.web.taxi.service.TimerService
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class TimerInitializer(
    val timerService: TimerService
) {

    @PostConstruct
    fun initTimer() {
        timerService.runTimer()
    }

}