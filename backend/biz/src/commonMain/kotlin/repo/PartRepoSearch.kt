package repo

import AppContext
import helpers.fail
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск в БД по фильтру"
    on { state == State.RUNNING }
    handle {
        val request = DbPartFilterRequest(
            nameFilter = partFilterValidated.searchString,
            ownerId = partFilterValidated.ownerId,
        )
        when(val result = partRepo.searchPart(request)) {
            is DbPartsResponseOk -> partsRepoDone = result.data.toMutableList()
            is DbPartsResponseErr -> fail(result.errors)
        }
    }
}
