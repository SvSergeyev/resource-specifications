package tech.sergeyev.education.app.spring.config

import CorSettings
import LoggerProvider
import PartProcessor
import mpLoggerLogback
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tech.sergeyev.education.app.spring.base.AppSettings
import tech.sergeyev.education.app.spring.base.SpringWsSessionRepo

@Suppress("unused")
@Configuration
class PartConfig {
    @Bean
    fun processor(corSettings: CorSettings) = PartProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun corSettings(): CorSettings = CorSettings(
        loggerProvider = loggerProvider(),
        wsSessions = wsRepo(),
    )

    @Bean
    fun appSettings(
        corSettings: CorSettings,
        processor: PartProcessor,
    ) = AppSettings(
        corSettings = corSettings,
        processor = processor,
    )

    @Bean
    fun wsRepo(): SpringWsSessionRepo = SpringWsSessionRepo()

}
