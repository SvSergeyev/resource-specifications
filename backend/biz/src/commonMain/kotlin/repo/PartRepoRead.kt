package repo

import AppContext
import helpers.fail
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение из БД"
    on { state == State.RUNNING }
    handle {
        val request = DbPartIdRequest(partValidated)
        when(val result = partRepo.readPart(request)) {
            is DbPartResponseOk -> partRepoRead = result.data
            is DbPartResponseErr -> fail(result.errors)
            is DbPartResponseErrWithData -> {
                fail(result.errors)
                partRepoRead = result.data
            }
        }
    }
}
