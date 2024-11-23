package tech.sergeyev.education.app.spring.config

import CorSettings
import tech.sergeyev.education.logging.common.LoggerProvider
import PartProcessor
import PartRepoInMemory
import PartRepoStub
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import tech.sergeyev.education.logging.jvm.mpLoggerLogback
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import repo.IRepoPart
import tech.sergeyev.education.app.spring.base.AppSettings
import tech.sergeyev.education.app.spring.base.SpringWsSessionRepo

@Suppress("unused")
@EnableConfigurationProperties(PartConfigPostgres::class)
@Configuration
class PartConfig(val postgresConfig: PartConfigPostgres) {
    val logger: Logger = LoggerFactory.getLogger(PartConfig::class.java)

    @Bean
    fun processor(corSettings: CorSettings) = PartProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun testRepo(): IRepoPart = PartRepoInMemory()

    @Bean
    fun prodRepo(): IRepoPart = RepoPartSql(postgresConfig.psql).apply {
        logger.info("Connecting to DB with ${this}")
    }
    @Bean
    fun stubRepo(): IRepoPart = PartRepoStub()

    @Bean
    fun corSettings(): CorSettings = CorSettings(
        loggerProvider = loggerProvider(),
        wsSessions = wsRepo(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo(),
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
