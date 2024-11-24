package repo

import AppContext
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.repoPrepareReport(title: String) = worker {
    this.title = title
    description = "prepare to report"
    on { state == State.RUNNING }
    handle {
        partRepoPrepare = partRepoRead.deepCopy()
        partRepoDone = partRepoRead.deepCopy()
    }
}
