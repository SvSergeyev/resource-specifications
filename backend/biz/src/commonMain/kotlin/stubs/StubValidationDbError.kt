package stubs

import AppContext
import helpers.fail
import models.State
import tech.sergeyev.education.api.v1.models.PartError
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { stubCase == Stubs.DB_ERROR && state == State.RUNNING }
    handle {
        fail(
            PartError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
