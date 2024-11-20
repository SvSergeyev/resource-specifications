package general

import AppContext
import models.Command
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.chain

fun ICorChainDsl<AppContext>.operation(
    title: String,
    command: Command,
    block: ICorChainDsl<AppContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == State.RUNNING }
}
