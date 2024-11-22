package tech.sergeyev.education.app.rabbit.config

import CorSettings
import IAppSettings
import PartProcessor

data class AppSettings(
    override val corSettings: CorSettings = CorSettings(),
    override val processor: PartProcessor = PartProcessor(corSettings),
    override val rabbit: RabbitConfig = RabbitConfig(),
    override val controllersConfigV1: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
    override val controllersConfigV2: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
): IAppSettings, IAppRabbitSettings
