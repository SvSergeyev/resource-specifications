package validation

import AppContext
import helpers.errorValidation
import helpers.fail
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.validateDescriptionNotEmpty(title: String) = worker {
    this.title = title
    on { partValidating.description.isEmpty() }
    handle {
        fail(
            errorValidation(
            field = "description",
            violationCode = "empty",
            description = "field must not be empty"
        )
        )
    }
}
