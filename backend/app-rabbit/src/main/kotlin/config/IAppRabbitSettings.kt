package tech.sergeyev.education.app.rabbit.config

import IAppSettings


interface IAppRabbitSettings: IAppSettings {
    val rabbit: RabbitConfig
    val controllersConfigV1: RabbitExchangeConfiguration
    val controllersConfigV2: RabbitExchangeConfiguration
}
