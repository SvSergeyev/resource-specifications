package validation

import AppContext
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.chain

fun ICorChainDsl<AppContext>.validation(block: ICorChainDsl<AppContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == State.RUNNING }
}
