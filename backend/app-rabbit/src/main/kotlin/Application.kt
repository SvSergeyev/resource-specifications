package tech.sergeyev.education.app.rabbit

import CorSettings
import LoggerProvider
import kotlinx.coroutines.runBlocking
import mpLoggerLogback
import tech.sergeyev.education.app.rabbit.config.AppSettings
import tech.sergeyev.education.app.rabbit.config.RabbitConfig
import tech.sergeyev.education.app.rabbit.mappers.fromArgs

fun main(vararg args: String) = runBlocking {
    val appSettings = AppSettings(
        rabbit = RabbitConfig.fromArgs(*args),
        corSettings = CorSettings(
            loggerProvider = LoggerProvider { mpLoggerLogback(it) }
        )
    )
    val app = RabbitApp(appSettings = appSettings, this)
    app.start()
}
