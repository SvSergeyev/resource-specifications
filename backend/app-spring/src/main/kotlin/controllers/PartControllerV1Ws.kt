package tech.sergeyev.education.app.spring.controllers

import AppContext
import apiV1Mapper
import controllerHelper
import fromTransport
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.runBlocking
import models.Command
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import tech.sergeyev.education.api.v1.models.IRequest
import tech.sergeyev.education.app.spring.base.AppSettings
import tech.sergeyev.education.app.spring.base.SpringWsSessionV1
import toTransportPart

@Component
class PartControllerV1Ws(private val appSettings: AppSettings) : WebSocketHandler {
    private val sessions = appSettings.corSettings.wsSessions

    override fun handle(session: WebSocketSession): Mono<Void> = runBlocking {
        val sess = SpringWsSessionV1(session)
        sessions.add(sess)
        val messageObj = process("ws-v1-init") {
            command = Command.INIT
            wsSession = sess
        }
        sess.send(messageObj)

        val messages = session.receive().asFlow()
            .map { message ->
                process("ws-v1-handle") {
                    wsSession = sess
                    val request = apiV1Mapper.readValue(message.payloadAsText, IRequest::class.java)
                    fromTransport(request)
                }
            }

        val output = merge(flowOf(messageObj), messages)
            .onCompletion {
                process("ws-v1-finish") {
                    wsSession = sess
                    command = Command.FINISH
                }
                sessions.remove(sess)
            }
            .map { session.textMessage(apiV1Mapper.writeValueAsString(it)) }
            .asFlux()
        session.send(output)
    }

    private suspend fun process(logId: String, function: AppContext.() -> Unit) = appSettings.controllerHelper(
        getRequest = function,
        toResponse = AppContext::toTransportPart,
        clazz = this@PartControllerV1Ws::class,
        logId = logId,
    )
}