package me.flashka.web.taxi.component

import me.flashka.web.taxi.service.TimerService
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class TimerComponent(
    val timerService: TimerService
) {

    @PostConstruct
    fun initTimer() {
        timerService.runTimer()
    }

}