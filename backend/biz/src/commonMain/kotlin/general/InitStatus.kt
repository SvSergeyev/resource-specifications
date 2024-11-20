package general

import AppContext
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.initStatus(title: String) = worker() {
    this.title = title
    this.description = """
        Этот обработчик устанавливает стартовый статус обработки. Запускается только в случае не заданного статуса.
    """.trimIndent()
    on { state == State.NONE }
    handle { state = State.RUNNING }
}
