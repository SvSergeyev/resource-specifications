package validation

import AppContext
import helpers.errorValidation
import helpers.fail
import models.PartId
import tech.sergeyev.education.cor.ICorChainDsl
import tech.sergeyev.education.cor.worker

fun ICorChainDsl<AppContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { partValidating.id != PartId.NONE && ! partValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = partValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
            field = "id",
            violationCode = "badFormat",
            description = "value $encodedId must contain only letters and numbers"
        )
        )
    }
}
