package stubs

import AppContext
import CorSettings
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker
import tech.sergeyev.education.logging.common.LogLevel

fun ICorChainDsl<AppContext>.stubSearchSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для поиска
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubs.stubs.stubSearchSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            partsResponse.addAll(PartStub.prepareSearchList(partFilterRequest.searchString))
        }
    }
}
