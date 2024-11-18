package config

import CorSettings
import LoggerProvider
import PartProcessor
import mpLoggerLogback
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@Configuration
class AdConfig {
    @Bean
    fun processor(corSettings: CorSettings) = PartProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun corSettings(): CorSettings = CorSettings(
        loggerProvider = loggerProvider(),
    )

    @Bean
    fun appSettings(
        corSettings: CorSettings,
        processor: PartProcessor,
    ) = AppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
