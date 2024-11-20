package validation

import AppContext
import helpers.errorValidation
import helpers.fail
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

// пример обработки ошибки в рамках бизнес-цепочки
fun ICorChainDsl<AppContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { partValidating.description.isNotEmpty() && !partValidating.description.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
