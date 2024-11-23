package repo

import AppContext
import helpers.fail
import helpers.errorSystem
import exceptions.PartDbNotConfiguredException
import models.WorkMode
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {
        partRepo = when {
            workMode == WorkMode.TEST -> corSettings.repoTest
            workMode == WorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != WorkMode.STUB && partRepo == IRepoPart.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = PartDbNotConfiguredException(workMode)
            )
        )
    }
}
