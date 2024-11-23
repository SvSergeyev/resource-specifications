package stubs

import AppContext
import helpers.fail
import models.State
import models.PartError
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для описания
    """.trimIndent()
    on { stubCase == Stubs.BAD_DESCRIPTION && state == State.RUNNING }
    handle {
        fail(
            PartError(
                group = "validation",
                code = "validation.validation-description",
                field = "description",
                message = "Wrong description field"
            )
        )
    }
}
