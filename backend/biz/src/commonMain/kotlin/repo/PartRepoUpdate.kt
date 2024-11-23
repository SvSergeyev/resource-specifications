package repo

import AppContext
import models.State
import helpers.fail
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        val request = DbPartRequest(partRepoPrepare)
        when(val result = partRepo.updatePart(request)) {
            is DbPartResponseOk -> partRepoDone = result.data
            is DbPartResponseErr -> fail(result.errors)
            is DbPartResponseErrWithData -> {
                fail(result.errors)
                partRepoDone = result.data
            }
        }
    }
}
