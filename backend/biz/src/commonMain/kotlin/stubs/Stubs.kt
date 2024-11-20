package stubs

import AppContext
import models.State
import models.WorkMode
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.chain

fun ICorChainDsl<AppContext>.stubs(title: String, block: ICorChainDsl<AppContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == WorkMode.STUB && state == State.RUNNING }
}
