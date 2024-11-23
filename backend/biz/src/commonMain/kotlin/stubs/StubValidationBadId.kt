package stubs

import AppContext
import helpers.fail
import models.State
import models.PartError
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для идентификатора
    """.trimIndent()
    on { stubCase == Stubs.BAD_ID && state == State.RUNNING }
    handle {
        fail(
            PartError(
                group = "validation",
                code = "validation.validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
