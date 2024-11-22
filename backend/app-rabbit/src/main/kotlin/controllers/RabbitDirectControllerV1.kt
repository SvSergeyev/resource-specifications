package tech.sergeyev.education.app.rabbit.controllers

import AppContext
import apiV1Mapper
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import controllerHelper
import fromTransport
import helpers.asPartError
import models.State
import tech.sergeyev.education.api.v1.models.IRequest
import tech.sergeyev.education.app.rabbit.config.AppSettings
import toTransportPart

// наследник RabbitProcessorBase, увязывает транспортную и бизнес-части
class RabbitDirectControllerV1(
    private val appSettings: AppSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfigV1,
    loggerProvider = appSettings.corSettings.loggerProvider,
) {
    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = apiV1Mapper.readValue(message.body, IRequest::class.java)
                fromTransport(req)
            },
            {
                val res = toTransportPart()
                apiV1Mapper.writeValueAsBytes(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
                }
            },
            this@RabbitDirectControllerV1::class,
            "rabbitmq-v1-processor"
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = AppContext()
        e.printStackTrace()
        context.state = State.FAILING
        context.errors.add(e.asPartError())
        val response = context.toTransportPart()
        apiV1Mapper.writeValueAsBytes(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
        }
    }
}
