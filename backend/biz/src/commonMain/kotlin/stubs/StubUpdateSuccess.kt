package stubs

import AppContext
import CorSettings
import models.PartId
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker
import tech.sergeyev.education.logging.common.LogLevel

fun ICorChainDsl<AppContext>.stubUpdateSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для изменения
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubs.stubs.stubUpdateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            val stub = PartStub.prepareResult {
                partRequest.id.takeIf { it != PartId.NONE }?.also { this.id = it }
                partRequest.name.takeIf { it.isNotBlank() }?.also { this.name = it }
                partRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                partRequest.materials.takeIf { it.isNotEmpty() }?.mapKeys { it.key.name }
            }
            partResponse = stub
        }
    }
}
