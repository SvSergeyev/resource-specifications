package repo

import AppContext
import models.State
import models.UserId
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == State.RUNNING }
    handle {
        partRepoPrepare = partValidated.deepCopy()
        partRepoPrepare.ownerId = UserId.NONE
    }
}
