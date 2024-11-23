package stubs

import AppContext
import helpers.fail
import models.State
import models.PartError
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.stubValidationBadName(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для name
    """.trimIndent()

    on { stubCase == Stubs.BAD_NAME && state == State.RUNNING }
    handle {
        fail(
            PartError(
                group = "validation",
                code = "validation.validation-name",
                field = "name",
                message = "Wrong name field"
            )
        )
    }
}
