package repo

import AppContext
import helpers.fail
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == State.RUNNING && partValidated.lock != partRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(partRepoRead, partValidated.lock).errors)
    }
}
