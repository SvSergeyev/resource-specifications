package tech.sergeyev.education.app.rabbit.controllers

import tech.sergeyev.education.app.rabbit.config.RabbitExchangeConfiguration

interface IRabbitMqController {
    val exchangeConfig: RabbitExchangeConfiguration
    suspend fun process()
    fun close()
}

