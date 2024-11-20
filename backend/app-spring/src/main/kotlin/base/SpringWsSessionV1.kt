package tech.sergeyev.education.app.spring.base

import apiV1ResponseSerialize
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import tech.sergeyev.education.api.v1.models.IResponse
import ws.IWsSession

data class SpringWsSessionV1(
    private val session: WebSocketSession,
) : IWsSession {
    override suspend fun <T> send(obj: T) {
        require(obj is IResponse)
        val message = apiV1ResponseSerialize(obj)
        println("SENDING to WsV1: $message")
        session.send(Mono.just(session.textMessage(message)))
    }
}