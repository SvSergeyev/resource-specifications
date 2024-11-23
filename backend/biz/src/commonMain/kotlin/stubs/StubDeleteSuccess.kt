package stubs

import AppContext
import CorSettings
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker
import tech.sergeyev.education.logging.common.LogLevel

fun ICorChainDsl<AppContext>.stubDeleteSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для удаления
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubs.stubs.stubDeleteSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            val stub = PartStub.prepareResult {
                partRequest.name.takeIf { it.isNotBlank() }?.also { this.name = it }
            }
            partResponse = stub
        }
    }
}
