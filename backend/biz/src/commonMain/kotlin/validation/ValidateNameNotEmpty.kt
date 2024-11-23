package validation

import AppContext
import helpers.errorValidation
import helpers.fail
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

// смотрим пример COR DSL валидации
fun ICorChainDsl<AppContext>.validateNameNotEmpty(title: String) = worker {
    this.title = title
    on { partValidating.name.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "name",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
