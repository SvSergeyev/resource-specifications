package tech.sergeyev.education.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import tech.sergeyev.education.app.spring.controllers.PartControllerV1Ws

@Suppress("unused")
@Configuration
class WebSocketConfig(
    private val partControllerV1: PartControllerV1Ws,
) {
    @Bean
    fun handlerMapping(): HandlerMapping {
        val handlerMap: Map<String, WebSocketHandler> = mapOf(
            "/v1/ws" to partControllerV1,
        )
        return SimpleUrlHandlerMapping(handlerMap, 1)
    }
}