package validation

import AppContext
import helpers.errorValidation
import helpers.fail
import models.PartLock
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { partValidating.lock != PartLock.NONE && !partValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = partValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
