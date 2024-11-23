package validation

import AppContext
import helpers.errorValidation
import helpers.fail
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { partValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
