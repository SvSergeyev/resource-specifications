package tech.sergeyev.education.app.rabbit

import kotlinx.coroutines.*
import tech.sergeyev.education.app.rabbit.config.AppSettings
import tech.sergeyev.education.app.rabbit.controllers.IRabbitMqController
import tech.sergeyev.education.app.rabbit.controllers.RabbitDirectControllerV1
import java.util.concurrent.atomic.AtomicBoolean

// Класс запускает все контроллеры
@OptIn(ExperimentalCoroutinesApi::class)
class RabbitApp(
    appSettings: AppSettings,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) : AutoCloseable {
    private val logger = appSettings.corSettings.loggerProvider.logger(this::class)
    private val controllers: List<IRabbitMqController> = listOf(
        RabbitDirectControllerV1(appSettings),
    )
    private val runFlag = AtomicBoolean(true)

    fun start() {
        runFlag.set(true)
        controllers.forEach {
            scope.launch(
                Dispatchers.IO.limitedParallelism(1) + CoroutineName("thread-${it.exchangeConfig.consumerTag}")
            ) {
                while (runFlag.get()) {
                    try {
                        logger.info("Process...${it.exchangeConfig.consumerTag}")
                        it.process()
                    } catch (e: RuntimeException) {
                        // логируем, что-то делаем
                        logger.error("Обработка завалена, возможно из-за потери соединения с RabbitMQ. Рестартуем")
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun close() {
        runFlag.set(false)
        controllers.forEach { it.close() }
        scope.cancel()
    }
}
