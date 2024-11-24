package repo

import AppContext
import helpers.fail
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление из БД по ID"
    on { state == State.RUNNING }
    handle {
        val request = DbPartIdRequest(partRepoPrepare)
        when(val result = partRepo.deletePart(request)) {
            is DbPartResponseOk -> partRepoDone = result.data
            is DbPartResponseErr -> {
                fail(result.errors)
                partRepoDone = partRepoRead
            }
            is DbPartResponseErrWithData -> {
                fail(result.errors)
                partRepoDone = result.data
            }
        }
    }
}
