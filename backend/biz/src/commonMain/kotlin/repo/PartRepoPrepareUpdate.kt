package repo

import AppContext
import models.State
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == State.RUNNING }
    handle {
        partRepoPrepare = partRepoRead.deepCopy().apply {
            this.name = partValidated.name
            description = partValidated.description
            materials = partValidated.materials
            lock = partValidated.lock
        }
    }
}
