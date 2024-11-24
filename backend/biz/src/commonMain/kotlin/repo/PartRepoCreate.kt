package repo

import AppContext
import helpers.fail
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление в БД"
    on { state == State.RUNNING }
    handle {
        val request = DbPartRequest(partRepoPrepare)
        when(val result = partRepo.createPart(request)) {
            is DbPartResponseOk -> partRepoDone = result.data
            is DbPartResponseErr -> fail(result.errors)
            is DbPartResponseErrWithData -> fail(result.errors)
        }
    }
}
